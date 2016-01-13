package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mauri.myemail.async.EmailReceiver;
import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.database.dataBase_string;
import com.example.mauri.myemail.model.account;
import com.example.mauri.myemail.model.email;
import com.example.mauri.myemail.model.emailSend;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    SharedPreferences pref = null;
    private static String PRIMO_AVVIO = "";
    private static int ACCOUNT_ATTUALE = -1;
    private SharedPreferences.Editor editor;
    private dataBaseManager db;
    int idAccount;

    private static int TIPO_LISTA = 0; //0 = ricevute, 1 = inviate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        pref = getPreferences(MODE_PRIVATE);

        Log.d("IDACCOUNTATTUALE", String.valueOf(ACCOUNT_ATTUALE));
        Intent accountCreato = getIntent();
        ACCOUNT_ATTUALE = accountCreato.getIntExtra("id", -1);
        Log.d("ID ACC", String.valueOf(ACCOUNT_ATTUALE));
        if (ACCOUNT_ATTUALE != -1) {
            editor = pref.edit();
            editor.putInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE);
            if (accountCreato.getIntExtra("TIPO_LISTA", -1) != -1) {
                TIPO_LISTA = accountCreato.getIntExtra("TIPO_LISTA", -1);
                editor.putInt("TIPO_LISTA", TIPO_LISTA);
                editor.commit();


            }
            editor.commit();

        }
        if (pref.contains("ACCOUNT_ATTUALE")) {
            // Istanzio il db manager
            db = new dataBaseManager(MainActivity.this);
            ImageButton newEmail = (ImageButton) findViewById(R.id.imgNewEmail);
            ImageButton reload = (ImageButton) findViewById(R.id.imgReload);

            newEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), newEmail.class);
                    i.putExtra("id", pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE));
                    startActivity(i);
                }
            });


            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("id", pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE));
                    startActivity(i);
                }
            });

            final Spinner spAccount = (Spinner) this.findViewById(R.id.spAccount);
            riempiSpinner(spAccount);
            spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String email = spAccount.getItemAtPosition(position).toString();
                    account a = (account) spAccount.getItemAtPosition(position);

                    int id_email = a.getId();
                    if (id_email != pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE)) {
                        Log.d("AAAAAAA", String.valueOf(id_email));
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("id", id_email);
                        startActivity(i);
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            EmailReceiver emailReceiver = new EmailReceiver();

            // Richiamo il download delle mail
            emailReceiver.downloadEmails(db.recuperaEmail(pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE)), db);

            //

            final ListView lvEmail = (ListView) findViewById(R.id.listView);
            // get data from the table by the ListAdapter
            ListAdapter customAdapter = new ListAdapter(this, R.layout.itemlistrow);
            lvEmail.setAdapter(customAdapter);
            if (pref.getInt("TIPO_LISTA", -1) == 0) {
                email[] emailArray = db.recuperaMailRicevute(pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE));
                for (int i = 0; i < emailArray.length; i++) {
                    email obj = emailArray[i];
                    customAdapter.add(obj);
                }

                lvEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        email e = (email) lvEmail.getItemAtPosition(position);
                        int idEmail = e.getId();
                        String from = e.getFrom();
                        int to = e.getTo();
                        String data = e.getData();
                        String oggetto = e.getOggetto();
                        String testo = e.getTesto();
                        int flagLetto = e.getFlag();

                        Intent i = new Intent(getApplicationContext(), emailRicevuta.class);
                        i.putExtra("idEmail", idEmail);
                        i.putExtra("from", from);
                        i.putExtra("to", to);
                        i.putExtra("data", data);
                        i.putExtra("oggetto", oggetto);
                        i.putExtra("testo", testo);
                        i.putExtra("flagLetto", flagLetto);
                        startActivityForResult(i, 10000);
                    }
                });
            } else if(pref.getInt("TIPO_LISTA", -1) == 1){
                ListAdapterSend customAdapterSend = new ListAdapterSend(this, R.layout.itemlistrow);
                lvEmail.setAdapter(customAdapterSend);

                emailSend[] emailArray = db.recuperaMailInviate(pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE));
                for (int i = 0; i < emailArray.length; i++) {
                    emailSend obj = emailArray[i];
                    customAdapterSend.add(obj);
                }

                lvEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        emailSend e = (emailSend) lvEmail.getItemAtPosition(position);
                        int idEmail = e.getId();
                        int from = e.getFrom();
                        String to = e.getTo();
                        String data = e.getData();
                        String oggetto = e.getOggetto();
                        String testo = e.getTesto();

                        Intent i = new Intent(getApplicationContext(), emailInviata.class);
                        i.putExtra("idEmail", idEmail);
                        i.putExtra("from", from);
                        i.putExtra("to", to);
                        i.putExtra("data", data);
                        i.putExtra("oggetto", oggetto);
                        i.putExtra("testo", testo);
                        startActivity(i);
                    }
                });

            }
        }
    }
    //riempe lo spinner
    public void riempiSpinner(Spinner spinner){

        ArrayAdapter ad = new ArrayAdapter<account>(this,R.layout.support_simple_spinner_dropdown_item);
        Cursor c;

        spinner.setAdapter(ad);
        c = db.query(dataBase_string.TBL_ACCOUNT);
        account attuale = null;
        try{
            while(c.moveToNext()){
                account a = new account(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getInt(4),c.getString(5),c.getInt(6),c.getInt(7),c.getString(8),c.getInt(9),
                        c.getInt(10),c.getString(11),c.getString(12));
                if(c.getInt(0) == ACCOUNT_ATTUALE)
                    attuale = a;
                ad.add(a);
            }
            spinner.setSelection(ad.getPosition(attuale));
            Log.d("non lo so","DENTRO SPINNER");
        }
        finally {
            c.close();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 10000){
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onResume(){
       if(pref.getInt("ACCOUNT_ATTUALE",ACCOUNT_ATTUALE) < 0) {
            editor = pref.edit();
            editor.putInt("TIPO_LISTA", TIPO_LISTA);
            editor.commit();
            Intent i = new Intent(this, accountSettings.class);
            startActivity(i);
        }

        super.onResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                if(pref != null && pref.getInt("TIPO_LISTA",-1) != 0) {
                    Log.d("ASDASD","CASE 1");
                    Intent i_r = new Intent(this, MainActivity.class);
                    i_r.putExtra("id", pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE));
                    i_r.putExtra("TIPO_LISTA", 0);
                    startActivity(i_r);
                }
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                if(pref != null && pref.getInt("TIPO_LISTA",-1) != 1) {
                    Log.d("ASDASD","CASE 2");
                    Intent i = new Intent(this, MainActivity.class);
                    int idAccount = pref.getInt("ACCOUNT_ATTUALE", ACCOUNT_ATTUALE);
                    i.putExtra("id", idAccount);
                    i.putExtra("TIPO_LISTA", 1);
                    startActivity(i);
                }
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

        if(TIPO_LISTA == 0)
            actionBar.setTitle("Ricevute");
        else
            actionBar.setTitle("Inviate");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                Intent i = new Intent(this, accountSettings.class);
                startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

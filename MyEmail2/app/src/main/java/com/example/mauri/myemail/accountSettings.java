package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.database.dataBase_string;
import com.example.mauri.myemail.model.account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauri on 02/08/2015.
 */
public class accountSettings extends Activity{

    dataBaseManager db = new dataBaseManager(this);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        final ListView lvAccount = (ListView) this.findViewById(R.id.lvAccount);
        riempiListView(lvAccount);


        //ListView list = (ListView) findViewById(R.id.listview);
        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = lvAccount.getItemAtPosition(position).toString();
                Intent i = new Intent(getApplicationContext(), accountModify.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

            //Gestione onClick sul bottone "Agggiungi Account"
            Button aggiungiAccount = (Button) this.findViewById(R.id.btAddAccount);
            aggiungiAccount.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                Intent i = new Intent(getApplicationContext(), configurationMain.class);
                startActivity(i);
                }
            }
            );

        //Gestione onClick sul bottone "Home"
        Button home = (Button) this.findViewById(R.id.btHome);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                Cursor c = null;
                if (db.numberOfAccount() > 0) {
                    c = db.query(dataBase_string.TBL_ACCOUNT);
                    c.moveToNext();
                    int idAccount = c.getInt(0);
                    i.putExtra("id", idAccount);
                }else{
                    int idAccount = -1;
                    i.putExtra("id", idAccount);
                }
            startActivity(i);
            }
        }
        );
        }

    //riempo la ListView con gli account che ho creato
    public void riempiListView(ListView lv){

        Cursor c;
        c = db.query(dataBase_string.TBL_ACCOUNT);

        List<account> arrayList = new ArrayList<account>();
        while(c.moveToNext()){
            account a = new account(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getInt(4),c.getString(5),c.getInt(6),c.getInt(7),c.getString(8),c.getInt(9),
                    c.getInt(10),c.getString(11),c.getString(12));
            arrayList.add(a);
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<account> ad = new ArrayAdapter<account>(
                this,
                android.R.layout.simple_list_item_1,
                arrayList );

        lv.setAdapter(ad);
    }
}

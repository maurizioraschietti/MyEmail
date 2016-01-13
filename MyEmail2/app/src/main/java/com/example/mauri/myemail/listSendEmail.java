package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.model.emailSend;

/**
 * Created by Mauri on 13/12/2015.
 */
public class listSendEmail extends Activity {
    dataBaseManager db = new dataBaseManager(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_send);
        Intent intent = getIntent();
        int idAccount = intent.getIntExtra("idAccount", -1);

        final ListView lvEmail = (ListView) findViewById(R.id.lvEmailRicevute);
        // get data from the table by the ListAdapter
        ListAdapterSend customAdapter = new ListAdapterSend(this, R.layout.itemlistrow);
        lvEmail .setAdapter(customAdapter);
        emailSend[] emailArray = db.recuperaMailInviate(idAccount);
        for(int i=0; i<emailArray.length ; i++){
            emailSend obj = emailArray[i];
            customAdapter.add(obj);
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

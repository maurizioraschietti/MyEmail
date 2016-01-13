package com.example.mauri.myemail;

/**
 * Created by Mauri on 22/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.mauri.myemail.async.EmailSender;
import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.database.dataBase_string;
import com.example.mauri.myemail.model.account;

public class emailAnswerSend extends Activity {
    dataBaseManager db = new dataBaseManager(this);
    String from;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_email);

        Intent i = getIntent();
        int tipoAnswer = i.getIntExtra("tipoAnswer", -1);
        final String fromFull = i.getStringExtra("from");
        if (fromFull.contains("<")) {
            from = fromFull.split("<")[1];
            from = from.split(">")[0];
        }else{
            from = fromFull;
        }
        final String to = i.getStringExtra("to");
        final String data = i.getStringExtra("data");
        final String oggetto = i.getStringExtra("oggetto");
        final String testo = i.getStringExtra("testo");
        Log.d("FROM", from);


        final EditText txtTo = (EditText) this.findViewById(R.id.txtTo);
        final EditText txtEmailObject = (EditText) this.findViewById(R.id.txtEmailObject);
        final EditText txtEmailText = (EditText) this.findViewById(R.id.txtEmailText);
        final Spinner spinnerAccount = (Spinner) this.findViewById(R.id.spinnerAccount);
        final WebView myWebView = (WebView) findViewById(R.id.webview);
        riempiSpinner(spinnerAccount);



        // e' una risposta
        if(tipoAnswer == 0) {
            txtTo.setText(from);
            txtEmailObject.setText("R: " + oggetto);
            if (testo.contains("<html>")) {
                myWebView.loadData(testo, "text/html", "utf-8");
                txtEmailText.setText(("\n\n\n---MESSAGGIO ORIGINALE---\n\nDa: " + from + "\nA: " + to + "\nData: " + data + "\nOggetto: " + oggetto + "\nTesto:\n "));

            }else{
                txtEmailText.setText(("\n\n\n---MESSAGGIO ORIGINALE---\n\nDa: " + from + "\nA: " + to + "\nData: " + data + "\nOggetto: " + oggetto + "\nTesto:\n "+ testo));
            }
        // e' un inoltro
        }else if(tipoAnswer == 1){
            txtEmailObject.setText("I: " + oggetto);
            if (testo.contains("<html>")) {
                myWebView.loadData(testo, "text/html", "utf-8");
                txtEmailText.setText(("\n\n\n---MESSAGGIO ORIGINALE---\n\nDa: " + from + "\nA: " + to + "\nData: " + data + "\nOggetto: " + oggetto + "\nTesto:\n "));

            }else{
                txtEmailText.setText(("\n\n\n---MESSAGGIO ORIGINALE---\n\nDa: " + from + "\nA: " + to + "\nData: " + data + "\nOggetto: " + oggetto + "\nTesto:\n"+ testo));
            } }

        Button btInvia = (Button) this.findViewById(R.id.btInvia);

        //Gestione onClick sul bottone "Invia"
        btInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( txtTo.getText().toString().isEmpty()==false && txtEmailObject.getText().toString().isEmpty()==false && txtEmailText.getText().toString().isEmpty()==false  ) {
                    if (function.isValidEmail(txtTo) == true) {
                        String emailTo = txtTo.getText().toString();
                        String emailFrom = (String) spinnerAccount.getSelectedItem();
                        String mailObject = txtEmailObject.getText().toString();
                        String mailText = txtEmailText.getText().toString();
                        if (testo.contains("<html>"))
                            mailText += testo;
                        EmailSender EmailSender = new EmailSender(db, emailTo, emailFrom, mailObject, mailText);
                        EmailSender.execute();
                        Context context = getApplicationContext();
                        CharSequence text = "Email Inviata!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Formato email non valido";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "Compila tutti i campi";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
    //riempe lo spinner
    public void riempiSpinner(Spinner spinner) {

        ArrayAdapter ad = new ArrayAdapter<account>(this, R.layout.support_simple_spinner_dropdown_item);
        Cursor c;

        spinner.setAdapter(ad);
        c = db.query(dataBase_string.TBL_ACCOUNT);

        try {
            while (c.moveToNext()) {
                account a = new account(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5),
                        c.getInt(6), c.getInt(7), c.getString(8), c.getInt(9), c.getInt(10), c.getString(11), c.getString(12));
                ad.add(a.getDesc());
            }
        } finally {
            c.close();
        }
    }
}

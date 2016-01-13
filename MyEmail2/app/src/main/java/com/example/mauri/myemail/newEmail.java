package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mauri.myemail.async.EmailSender;
import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.database.dataBase_string;
import com.example.mauri.myemail.model.account;

/**
 * Created by Mauri on 05/08/2015.
 */
public class newEmail extends Activity{
    dataBaseManager db = new dataBaseManager(this);
    int AccountAttuale;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_email);

        Intent i = getIntent();
        AccountAttuale = i.getIntExtra("id",-1);
        String destinatario = i.getStringExtra("mailTo");
        final EditText txtTo = (EditText) this.findViewById(R.id.txtTo);
        final Spinner spinnerAccount = (Spinner) this.findViewById(R.id.spinnerAccount);
        riempiSpinner(spinnerAccount);
        final EditText txtEmailObject = (EditText) this.findViewById(R.id.txtEmailObject);
        final EditText txtEmailText = (EditText) this.findViewById(R.id.txtEmailText);

        if(destinatario != ""){
            txtTo.setText(destinatario);
        }


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
                        EmailSender EmailSender = new EmailSender(db, emailTo, emailFrom, mailObject, mailText);
                        EmailSender.execute();
                        Context context = getApplicationContext();
                        CharSequence text = "Email Inviata!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("id",AccountAttuale);
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
    public void riempiSpinner(Spinner spinner){

        ArrayAdapter ad = new ArrayAdapter<account>(this,R.layout.support_simple_spinner_dropdown_item);
        Cursor c;

        spinner.setAdapter(ad);
        c = db.query(dataBase_string.TBL_ACCOUNT);
        String attuale = null;
        try{
            while(c.moveToNext()){
                account a = new account(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getInt(4),c.getString(5),
                        c.getInt(6),c.getInt(7),c.getString(8),c.getInt(9),c.getInt(10),c.getString(11),c.getString(12));
                if(c.getInt(0) == AccountAttuale)
                    attuale = a.getDesc();
                ad.add(a.getDesc());
            }
            spinner.setSelection(ad.getPosition(attuale));

        }
        finally {
            c.close();
        }
    }
}

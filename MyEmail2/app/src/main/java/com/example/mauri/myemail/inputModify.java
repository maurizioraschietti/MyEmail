package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.database.dataBase_string;
import com.example.mauri.myemail.model.tipoDiProtezione;

/**
 * Created by Mauri on 21/08/2015.
 */
public class inputModify extends Activity {
    dataBaseManager db = new dataBaseManager(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_modify);


        final EditText txtEmail = (EditText) this.findViewById(R.id.txtEmail);
        final EditText txtName = (EditText) this.findViewById(R.id.txtName);
        final EditText txtPassword = (EditText) this.findViewById(R.id.txtPassword);
        TextView serverTitle = (TextView) this.findViewById(R.id.serverTitle);
        final EditText txtServerInput = (EditText) this.findViewById(R.id.txtServerInput);
        final EditText txtPortInput = (EditText) this.findViewById(R.id.txtPortInput);
        final Spinner spinnerProtType = (Spinner) this.findViewById(R.id.spinnerProtType);

        riempiSpinner(spinnerProtType);
        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        Cursor c = db.recuperaAccountbyEmail(email);

        String password = "";
        String username = "";
        String host = "";
        String port = "";
        String protocol = "";
        while (c.moveToNext()) {
            CryptoUtil cu = new CryptoUtil();
            String passwordCrypt = c.getString(2);
            password = cu.simpleStringDecrypt(passwordCrypt);
            username = c.getString(4);
            host = c.getString(5);
            int portInt = c.getInt(7);
            port = Integer.toString(portInt);
            int protocolInt = c.getInt(3);
            int prot = c.getInt(6);
            protocol = Integer.toString(protocolInt);

            txtEmail.setText(email);
            txtName.setText(username);
            txtPassword.setText(password);
            txtServerInput.setText(host);
            txtPortInput.setText(port);
            txtEmail.setEnabled(false);
            txtName.setEnabled(false);
            spinnerProtType.setSelection(prot);

            if (protocolInt == 0) {
                serverTitle.setText("Server POP3");
            } else if (protocolInt == 1) {
                serverTitle.setText("Server IMAP");
            }


        }


        //Gestione onClick sul bottone "Fine"
        Button btFineIn  = (Button) this.findViewById(R.id.btFineIn);
        btFineIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPassword.getText().toString().isEmpty() == false && txtServerInput.getText().toString().isEmpty() == false && txtPortInput.getText().toString().isEmpty() == false) {
                    tipoDiProtezione tp = (tipoDiProtezione) spinnerProtType.getSelectedItem();
                    CryptoUtil cu= new CryptoUtil();
                    String passwordCrypt = cu.simpleStringEncrypt(txtPassword.getText().toString());
                    db.aggiornaInput(txtEmail.getText().toString(),passwordCrypt,txtServerInput.getText().toString(),tp.getId(),Integer.parseInt(txtPortInput.getText().toString()));
                    Intent i = new Intent(getApplicationContext(), accountSettings.class);
                    i.putExtra("email",email);
                    startActivity(i);

                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Compila tutti i campi";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

    });


        CheckBox cbShowPassword = (CheckBox) this.findViewById(R.id.cbShowPassword);
        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    txtPassword.setInputType(129);
                }
            }
        });
    }


            public void riempiSpinner(Spinner spinner) {

                ArrayAdapter ad = new ArrayAdapter<tipoDiProtezione>(this, R.layout.support_simple_spinner_dropdown_item);
                Cursor c;

                spinner.setAdapter(ad);
                c = db.query(dataBase_string.TBL_TIPOPROTEZIONE);

                try {
                    while (c.moveToNext()) {
                        tipoDiProtezione tp = new tipoDiProtezione(c.getInt(0), c.getString(1));
                        ad.add(tp);
                    }
                } finally {
                    c.close();
                }
            }
        }

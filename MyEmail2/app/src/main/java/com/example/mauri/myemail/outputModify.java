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
import android.widget.Toast;

import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.database.dataBase_string;
import com.example.mauri.myemail.model.tipoDiProtezione;

/**
 * Created by Mauri on 21/08/2015.
 */
public class outputModify extends Activity {
    dataBaseManager db = new dataBaseManager(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_modify);

        final EditText txtSMTP = (EditText) this.findViewById(R.id.txtSMTP);
        final EditText txtPortOu = (EditText) this.findViewById(R.id.txtPortOu);
        final EditText txtNameOut = (EditText) this.findViewById(R.id.txtName);
        final EditText txtPasswordOut = (EditText) this.findViewById(R.id.txtPassword);
        final CheckBox cbAccesso = (CheckBox) this.findViewById(R.id.cbAccesso);
        cbAccesso.setVisibility(View.INVISIBLE);
        final Spinner spinnerProtType = (Spinner) this.findViewById(R.id.spinnerProtType);

        riempiSpinner(spinnerProtType);
        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        Cursor c = db.recuperaAccountbyEmail(email);


        String password = "";
        String username = "";
        String host = "";
        String port = "";
        while (c.moveToNext()) {
            CryptoUtil cu = new CryptoUtil();
            String passwordCrypt = c.getString(12);
            password = cu.simpleStringDecrypt(passwordCrypt);
            username = c.getString(11);
            host = c.getString(8);
            int portInt = c.getInt(10);
            port = Integer.toString(portInt);
            int prot = c.getInt(9);

            /*if(username.isEmpty()==true && password.isEmpty()==true){
                txtNameOut.setEnabled(false);
                txtPasswordOut.setEnabled(false);
            }else{
                cbAccesso.setChecked(true);
            }*/

            txtNameOut.setText(username);
            txtPasswordOut.setText(password);
            txtSMTP.setText(host);
            txtPortOu.setText(port);
            spinnerProtType.setSelection(prot);
        }
        txtNameOut.setEnabled(false);
        txtPasswordOut.setEnabled(false);




        //Gestione onClick sul bottone "Fine"
        Button btFineOu = (Button) this.findViewById(R.id.btFineOu);
        btFineOu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSMTP.getText().toString().isEmpty() == false && txtPortOu.getText().toString().isEmpty() == false) {
                    //if (cbAccesso.isChecked()) {
                        if (txtNameOut.getText().toString().isEmpty() == false && txtPasswordOut.getText().toString().isEmpty() == false) {
                            if (function.isValidEmail(txtNameOut) == true) {
                                tipoDiProtezione tp = (tipoDiProtezione) spinnerProtType.getSelectedItem();
                                CryptoUtil cu= new CryptoUtil();
                                String passwordCrypt = cu.simpleStringEncrypt(txtPasswordOut.getText().toString());
                                db.aggiornaOutput(txtNameOut.getText().toString(),txtSMTP.getText().toString(), tp.getId(), Integer.parseInt(txtPortOu.getText().toString()),txtNameOut.getText().toString(), passwordCrypt);
                                Intent i = new Intent(getApplicationContext(), accountSettings.class);
                                i.putExtra("email",email);
                                startActivity(i);

                            }else{
                                Context context = getApplicationContext();
                                CharSequence text = "Formato email non valido";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "Compila tutti i campi";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Compila tutti i campi";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
/*
        cbAccesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtNameOut.setEnabled(true);
                    txtPasswordOut.setEnabled(true);
                } else {
                    txtNameOut.setEnabled(false);
                    txtPasswordOut.setEnabled(false);
                }
            }
        });
*/

        CheckBox cbShowPassword = (CheckBox) this.findViewById(R.id.cbShowPassword);
        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtPasswordOut.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    txtPasswordOut.setInputType(129);
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


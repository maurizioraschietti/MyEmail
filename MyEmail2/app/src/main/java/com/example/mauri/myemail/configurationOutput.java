package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import java.security.MessageDigest;

/**
 * Created by Mauri on 04/08/2015.
 */
public class configurationOutput extends Activity {
    dataBaseManager db = new dataBaseManager(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_output);

        Intent i = getIntent();
        final String email = i.getStringExtra("email");
        final String password = i.getStringExtra("password");
        final String serverInput = i.getStringExtra("server_input");
        final int serverType = i.getIntExtra("server_type", -1);
        final int protType = i.getIntExtra("prot_type", -1);
        final int portInput = i.getIntExtra("port_input", -1);

        final EditText txtSMTP = (EditText) this.findViewById(R.id.txtSMTP);
        final EditText txtPortOu = (EditText) this.findViewById(R.id.txtPortOu);
        final EditText txtNameOut = (EditText) this.findViewById(R.id.txtName);
        final EditText txtPasswordOut = (EditText) this.findViewById(R.id.txtPassword);
        final Spinner spinnerProtType = (Spinner) this.findViewById(R.id.spinnerProtType);
        final CheckBox cbAccesso = (CheckBox) this.findViewById(R.id.cbAccesso);
        cbAccesso.setVisibility(View.INVISIBLE);

        txtNameOut.setText(email);
        txtPasswordOut.setText(password);

        txtNameOut.setEnabled(false);
        txtPasswordOut.setEnabled(false);

        riempiSpinner(spinnerProtType);

        Button btNextOu = (Button) this.findViewById(R.id.btNextOu);
        btNextOu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSMTP.getText().toString().isEmpty() == false && txtPortOu.getText().toString().isEmpty() == false) {
                    if (txtNameOut.getText().toString().isEmpty() == false && txtPasswordOut.getText().toString().isEmpty() == false) {
                        if (function.isValidEmail(txtNameOut) == true) {
                            tipoDiProtezione tp = (tipoDiProtezione) spinnerProtType.getSelectedItem();
                            MessageDigest ms;

                            CryptoUtil cu= new CryptoUtil();
                            String passwordCrypt = cu.simpleStringEncrypt(txtPasswordOut.getText().toString());
                            Log.d("AAAAAAAAAAAA", passwordCrypt);
                            String dateUltimoAggiornamento = "2015-11-01 00:00:00";
                            int idAccount = db.salvaAccount(email, email, passwordCrypt, serverType, serverInput, protType, portInput, txtSMTP.getText().toString(), tp.getId(), Integer.parseInt(txtPortOu.getText().toString()),
                                    txtNameOut.getText().toString(), passwordCrypt,dateUltimoAggiornamento);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("id", idAccount);
                            startActivity(i);

                        } else {
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


        //checkbox per mostrare password
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
    }

    //riempe lo spinner
    public void riempiSpinner(Spinner spinner){

        ArrayAdapter ad = new ArrayAdapter<tipoDiProtezione>(this,R.layout.support_simple_spinner_dropdown_item);
        Cursor c;

        spinner.setAdapter(ad);
        c = db.query(dataBase_string.TBL_TIPOPROTEZIONE);

        try{
            while(c.moveToNext()){
                tipoDiProtezione tp = new tipoDiProtezione(c.getInt(0),c.getString(1));
                ad.add(tp);
            }
        }
        finally {
            c.close();
        }
    }
}

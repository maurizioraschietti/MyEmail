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
 * Created by Mauri on 04/08/2015.
 */
public class configurationInput extends Activity {
    dataBaseManager db = new dataBaseManager(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_input);

        final EditText txtEmail = (EditText) this.findViewById(R.id.txtEmail);
        final EditText txtName = (EditText) this.findViewById(R.id.txtName);
        final EditText txtPassword = (EditText) this.findViewById(R.id.txtPassword);
        TextView serverTitle = (TextView) this.findViewById(R.id.serverTitle);
        final EditText txtServerInput = (EditText) this.findViewById(R.id.txtServerInput);
        final EditText txtPortInput = (EditText) this.findViewById(R.id.txtPortInput);
            final Spinner spinnerProtType = (Spinner) this.findViewById(R.id.spinnerProtType);

            riempiSpinner(spinnerProtType);

        Intent i = getIntent();
        final int server = i.getIntExtra("server_type", -1);
        String email = i.getStringExtra("email");
        String password = i.getStringExtra("password");

        txtEmail.setText(email);
        txtName.setText(email);
        txtPassword.setText(password);
        txtEmail.setEnabled(false);
        txtName.setEnabled(false);
        txtPassword.setEnabled(false);

        if (server==0){
            serverTitle.setText("Server POP3");
        }
        else if(server==1){
            serverTitle.setText("Server IMAP");
        }

        //Gestione onClick sul bottone "Next"
        Button btNextIn = (Button) this.findViewById(R.id.btNextIn);
        btNextIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( txtEmail.getText().toString().isEmpty()==false && txtName.getText().toString().isEmpty()==false && txtPassword.getText().toString().isEmpty()==false  &&
                    txtServerInput.getText().toString().isEmpty()==false && txtPortInput.getText().toString().isEmpty()==false ) {
                        Intent i = new Intent(getApplicationContext(), configurationOutput.class);
                        i.putExtra("email",txtEmail.getText().toString());
                        i.putExtra("password",txtPassword.getText().toString());
                        i.putExtra("server_input",txtServerInput.getText().toString());
                        i.putExtra("server_type",server);
                        tipoDiProtezione tp = (tipoDiProtezione) spinnerProtType.getSelectedItem();
                        i.putExtra("prot_type",tp.getId());
                        i.putExtra("port_input", Integer.parseInt(txtPortInput.getText().toString()));
                        startActivity(i);

                    }else {
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


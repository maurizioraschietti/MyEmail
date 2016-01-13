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

/**
 * Created by Mauri on 03/08/2015.
 */
public class configurationMain extends Activity {
    dataBaseManager db = new dataBaseManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_main);

        final EditText txtEmail = (EditText) this.findViewById(R.id.txtEmail);
        final EditText txtPassword = (EditText) this.findViewById(R.id.txtPassword);

        //Gestione onClick sul bottone "Next"
        final Button btNextMain = (Button) this.findViewById(R.id.btNextMain);
        btNextMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtEmail.getText().toString().isEmpty() == false && txtPassword.getText().toString().isEmpty() == false) {
                    if (function.isValidEmail(txtEmail) == true) {
                        if (db.existEmail(txtEmail.getText().toString()) == false) {
                            Intent i = new Intent(getApplicationContext(), configurationType.class);
                            i.putExtra("email", txtEmail.getText().toString());
                            i.putExtra("password", txtPassword.getText().toString());
                            startActivity(i);
                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "Email gia presente";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
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
                }
            }

            );

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
                                                  }

        );
    }
}



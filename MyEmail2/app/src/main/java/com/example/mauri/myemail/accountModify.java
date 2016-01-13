package com.example.mauri.myemail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mauri.myemail.database.dataBaseManager;

/**
 * Created by Mauri on 20/08/2015.
 */
public class accountModify extends Activity {
    dataBaseManager db = new dataBaseManager(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_modify);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");

        Button btEntrata = (Button) this.findViewById(R.id.btEntrata);
        Button btUscita = (Button) this.findViewById(R.id.btUscita);
        Button btElimina = (Button) this.findViewById(R.id.btElimina);

        //Gestione onClick sul bottone "Impostazioni server in entrata"
        btEntrata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), inputModify.class);
                i.putExtra("email", email);

                startActivity(i);
            }
        });

        //Gestione onClick sul bottone "Impostazioni server in uscita"
        btUscita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), outputModify.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

        //Gestione onClick sul bottone "Elimina Account"
        btElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                int a = db.numberOfAccount();
                                if(a<= 1){
                                    Context context = getApplicationContext();
                                    CharSequence text = "Devi avere almeno un account di posta!";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    break;
                                }else {
                                    db.deleteAccount(email);
                                    Intent i = new Intent(getApplicationContext(), accountSettings.class);
                                    startActivity(i);
                                    break;
                                }

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }
}

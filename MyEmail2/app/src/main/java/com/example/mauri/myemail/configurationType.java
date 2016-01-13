package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mauri on 03/08/2015.
 */
public class configurationType extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_type);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");

        Button btPOP3 = (Button) this.findViewById(R.id.btPOP3);
        Button btIMAP = (Button) this.findViewById(R.id.btIMAP);

        //Gestione onClick sul bottone "POP3"
        btPOP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), configurationInput.class);
                i.putExtra("server_type",0);
                i.putExtra("email",email);
                i.putExtra("password",password);
                startActivity(i);

            }
        });

        //Gestione onClick sul bottone "IMAP"
        btIMAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), configurationInput.class);
                i.putExtra("server_type", 1);
                i.putExtra("email",email);
                i.putExtra("password",password);
                startActivity(i);
            }
        });
    }
}

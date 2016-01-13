package com.example.mauri.myemail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Mauri on 22/12/2015.
 */
public class EmailSenderReceiver extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_email);

        Intent i = getIntent();
        String mailTo = i.getData().getSchemeSpecificPart();

        Intent intent = new Intent(getApplicationContext(), newEmail.class);
        intent.putExtra("mailTo",mailTo);
        startActivity(intent);

        Log.d("MailTo", mailTo);
    }
}

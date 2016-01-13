package com.example.mauri.myemail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mauri.myemail.database.dataBaseManager;

/**
 * Created by Mauri on 09/12/2015.
 */
public class emailRicevuta extends Activity {
    dataBaseManager db = new dataBaseManager(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_ricevuta);

        Intent i = getIntent();

        final int idEmail = i.getIntExtra("idEmail", -1);
        final String from = i.getStringExtra("from");
        final int to = i.getIntExtra("to", -1);
        final String data = i.getStringExtra("data");
        final String oggetto = i.getStringExtra("oggetto");
        final String testo = i.getStringExtra("testo");
        final int flagLetto = i.getIntExtra("flagLetto", -1);

        final TextView txtFrom = (TextView) this.findViewById(R.id.txtFrom);
        final TextView txtTo = (TextView) this.findViewById(R.id.txtTo);
        final TextView txtData = (TextView) this.findViewById(R.id.txtData);
        final TextView txtOggetto = (TextView) this.findViewById(R.id.txtOggetto);
        final TextView txtTesto = (TextView) this.findViewById(R.id.txtTesto);

        final ImageButton imgAnswer = (ImageButton) this.findViewById(R.id.imgAnswer);
        final ImageButton imgForward = (ImageButton) this.findViewById(R.id.imgForward);
        final ImageButton imgDelete = (ImageButton) this.findViewById(R.id.imgDelete);



        txtFrom.setText(from);
        String[] email = db.recuperaEmail(to);
        txtTo.setText(email[3]);
        txtData.setText(data);
        txtOggetto.setText(oggetto);
        if (testo.contains("<html")) {
            String[] arrayTesto = testo.split("<html");
            txtTesto.setText(arrayTesto[0]);
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadData("<html"+arrayTesto[1], "text/html", "utf-8");
        }else{
            txtTesto.setText(testo);
        }

        db.updateFlag(idEmail);

        imgAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), emailAnswer.class);
                i.putExtra("tipoAnswer", 0);
                i.putExtra("from", from);
                i.putExtra("to", to);
                i.putExtra("data", data);
                i.putExtra("oggetto", oggetto);
                i.putExtra("testo", testo);
                startActivity(i);
            }
        });

        imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), emailAnswer.class);
                i.putExtra("tipoAnswer",1);
                i.putExtra("from", from);
                i.putExtra("to", to);
                i.putExtra("data", data);
                i.putExtra("oggetto", oggetto);
                i.putExtra("testo", testo);
                startActivity(i);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                db.deleteReceivedEmail(idEmail);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.putExtra("id",to);
                                startActivity(i);
                                break;

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

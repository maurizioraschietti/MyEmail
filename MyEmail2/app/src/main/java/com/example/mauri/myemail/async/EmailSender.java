package com.example.mauri.myemail.async;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mauri.myemail.CryptoUtil;
import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.model.emailSendingBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// ...

/**
 * Created by Mauri on 12/11/2015.
 */
public class EmailSender extends AsyncTask<Void,Void,Void>{

    private dataBaseManager db;
    private String emailTo;
    private String emailFrom;
    private String emailObject;
    private String emailText;
    int tipoProtezione;


    public EmailSender(dataBaseManager db, String emailTo, String emailFrom, String emailObject, String emailText){
        this.db = db;
        this.emailTo = emailTo;
        this.emailFrom = emailFrom;
        this.emailObject = emailObject;
        this.emailText = emailText;

    }

    private void sendMail(dataBaseManager db, String emailTo, String emailFrom, String emailObject, String emailText) {
        Cursor c = db.recuperaAccountbyEmail(emailFrom);

        while(c.moveToNext()){
            tipoProtezione = c.getInt(9);
        }
        // Recipient's email ID needs to be mentioned.
        String to = emailTo;

        // Sender's email ID needs to be mentioned
        String from = emailFrom;

        // Host
        String host = db.recuperaHost(from);

        // Get system properties
        Properties properties = System.getProperties();
        //è ssl
        if(tipoProtezione == 1){
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        //è tls
        else if (tipoProtezione ==3){
            properties.put("mail.smtp.starttls.enable", "true");
        }

        properties.put("mail.smtp.auth", true);


        int port = db.recuperaPort(from);
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.starttls.enable", "true");


        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(emailObject);

            // Now set the actual message
            message.setText(emailText);

            //Encrypt
            String passwordCrypt = db.recuperaPassword(from);
            CryptoUtil cu = new CryptoUtil();
            String password = cu.simpleStringDecrypt(passwordCrypt);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, emailFrom, password);
            Log.d("TENTATIVO_CONNESSIONE", host);
            Log.d("TENTATIVO_CONNESSIONE", String.valueOf(port));
            Log.d("TENTATIVO_CONNESSIONE", emailFrom);
            Log.d("TENTATIVO_CONNESSIONE", password);

            message.saveChanges();

            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Sent message successfully....");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            transport.close();

            emailSendingBean ms = new emailSendingBean();
            ms.setTo(to);
            ms.setFrom(from);
            ms.setOggetto(emailObject);
            ms.setCorpo(emailText);
            ms.setDate(currentTimeStamp);
            db.saveSending(ms);

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        sendMail(this.db, this.emailTo, this.emailFrom, this.emailObject, this.emailText);
        return null;
    }
}


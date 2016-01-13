package com.example.mauri.myemail.async;

/**
 * Created by Mauri on 13/08/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.mauri.myemail.MailSSLSocketFactory;
import com.example.mauri.myemail.database.dataBaseManager;
import com.example.mauri.myemail.model.emailReceivingBean;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import static javax.mail.Flags.*;

/**
 * This program demonstrates how to get e-mail messages from a POP3/IMAP server
 *
 * @author www.codejava.net
 */
public class EmailReceiver {


    AsyncTask task;
    private boolean textIsHtml = false;


    /**
     * Returns a Properties object which is configured for a POP3/IMAP server
     *
     * @param protocol either "imap" or "pop3"
     * @param host
     * @param port
     * @return a Properties object
     */

    public Properties getServerProperties(String protocol, String host, String port, int inputProtection) {
        Properties properties = new Properties();



        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        //è ssl
        if(inputProtection == 1){
            properties.setProperty(String.format("mail.%s.ssl.enable", protocol+"s"), "true");
        }
        //è tls
        else if (inputProtection ==3){
            properties.setProperty(String.format("mail.%s.tsl.enable", protocol+"s"), "true");
        }


        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));
        return properties;
    }

    /**
     * Downloads new messages and fetches details for each message.
     *
     * @param protocol
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    private class Connessione extends AsyncTask<String, Void, Message[]> {
        private dataBaseManager db;


        public Connessione(dataBaseManager db) {
            this.db = db;
        }

        @Override
        protected Message[] doInBackground(String... connectionArray) {

            String protocol = connectionArray[0].trim();
            String host = connectionArray[1].trim();
            String port = connectionArray[2].trim();
            String userName = connectionArray[3].trim();
            String password = connectionArray[4].trim();
            String inputProtection = connectionArray[5].trim();

            int inputProt = Integer.parseInt(inputProtection);
            if (inputProt == 1 && protocol.equals("imap")){
                protocol = protocol+"s";
            }

            Properties properties = getServerProperties(protocol, host, port, inputProt);

            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            sf.setTrustAllHosts(true);
            properties.put(String.format("mail.%s.socketFactory.class", protocol), sf);

            Session session = Session.getDefaultInstance(properties);

            try {
                //stampe controllo connessione
                Log.d("TENTATIVO_CONNESSIONE", protocol);
                Log.d("TENTATIVO_CONNESSIONE", host);
                Log.d("TENTATIVO_CONNESSIONE", port);
                Log.d("TENTATIVO_CONNESSIONE", userName);
                Log.d("TENTATIVO_CONNESSIONE", password);

                // connects to the message store
                Store store = session.getStore(protocol);
                store.connect(userName, password);

                //stampe controllo connessione
                Log.d("CONNESSIONE_AVVENUTA", "");
                Log.d("TENTATIVO_INBOX", "");

                // opens the inbox folder
                Folder folderInbox = store.getFolder("INBOX");
                folderInbox.open(Folder.READ_ONLY);

                //stampe controllo connessione
                Log.d("APERTURA_INBOX", "");
                Log.d("TENTATIVO_MESSAGGI", "");


                Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
                //Log.d("STAMPA_MESSAGGIO", messages[1].toString());


                for (int i = messages.length - 1; i > messages.length - 40 && i >= 0; i--) {
                    Message msg = messages[i];
                    Date dataMessaggio = msg.getSentDate();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dataMessaggioCorrente = dateFormat.format(dataMessaggio); // Find todays date
                    String ultimoAggiornamento = db.recuperaData(userName);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date dateUltimoAggiornamento = format.parse(ultimoAggiornamento);
                        if (dataMessaggio.after(dateUltimoAggiornamento)) {




                    Address[] fromAddress = msg.getFrom();
                    String from = fromAddress[0].toString();
                    String subject = msg.getSubject();
                    String toList = parseAddresses(msg.getRecipients(RecipientType.TO));

                    Flags flagsList = msg.getFlags();

                    String messageContent = "";
                    String messageContentFull = "";
                    Object content;
                    try {
                        content = msg.getContent();
                        if (content instanceof String) {
                            messageContent = content.toString();
                        } else if (content instanceof Multipart) {
                            MimeMultipart multiPart = (MimeMultipart) msg.getContent();

                            for(int a = 0; a<multiPart.getCount(); a++){
                                content = multiPart.getBodyPart(a).getContent();
                                messageContentFull = messageContentFull + content.toString();
                                /*link di riferimento
                                * http://stackoverflow.com/questions/13474705/reading-body-part-of-a-mime-multipart
                                * http://www.oracle.com/technetwork/java/javamail/faq/index.html#mainbody
                                */
                              }
                            if (messageContentFull.contains("<html>")) {
                                messageContent = messageContentFull.split("<html>")[1];
                                messageContent = "<html>" + messageContent;
                            } else {
                                messageContent = messageContentFull;
                            }
                        }
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }


                    // print out details of each message
                    /*
                    System.out.println("Message #" + (i + 1) + ":");
                    System.out.println("\t From: " + from);
                    System.out.println("\t To: " + toList);
                    System.out.println("\t CC: " + ccList);
                    System.out.println("\t Subject: " + subject);
                    System.out.println("\t Sent Date: " + sentDate);
                    System.out.println("\t Message: " + messageContent);*/

                    emailReceivingBean mr = new emailReceivingBean();
                    mr.setFrom(from);
                    mr.setTo(toList);
                    mr.setOggetto(subject);
                    mr.setDate(dataMessaggioCorrente);
                    mr.setCorpo(messageContent);
                    mr.setFlagLetto(0);

                    //se il protocollo e' pop 3
                 //   if (protocol.equals("pop3")) {

                   /* } else if (protocol.equals("imaps")||protocol.equals("imap")) {
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        db.saveReceiving(mr, userName);
                    }*/
                            db.saveReceiving(mr, userName);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }//end for
                //update ultimo aggiornamento
                String dataAttuale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                db.updateUltimoAggiornamento(userName, dataAttuale);


                // disconnect
                folderInbox.close(false);
                store.close();

                return messages;
            } catch (NoSuchProviderException ex) {
                System.out.println("No provider for protocol: " + protocol);
                ex.printStackTrace();
            } catch (MessagingException ex) {
                System.out.println("Could not connect to the message store");
                ex.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }


    }

    /**
     * Effettua il download delle mail
     *
     * @param datiConnessione
     * @param db              Oggetto Db manager passato dall'activity
     */
    public void downloadEmails(String[] datiConnessione, dataBaseManager db) {
        task = new Connessione(db).execute(datiConnessione);
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }
}
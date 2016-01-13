package com.example.mauri.myemail.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.mauri.myemail.CryptoUtil;
import com.example.mauri.myemail.model.email;
import com.example.mauri.myemail.model.emailReceivingBean;
import com.example.mauri.myemail.model.emailSend;
import com.example.mauri.myemail.model.emailSendingBean;

import java.text.SimpleDateFormat;

/**
 * Created by Mauri on 02/08/2015.
 */
public class dataBaseManager {
    private dataBase database;

    /*Costruttore*/
    public dataBaseManager(Context ctx) {
        database = new dataBase(ctx);
    }


    public int salvaAccount(String email, String nomeUtenteInput, String password, int tipoServerInput, String serverInput, int tipoProtezioneInput,
                            int portInput, String serverOutput, int tipoProtezioneOutput, int portOutput, String nomeUtenteOutput, String passwordOutput, String dataUltimoAggiornamento) {
         /*Recupera un riferimento al database che ne permette anche la modifica*/
        SQLiteDatabase db = database.getWritableDatabase();
        /*Riempo la tabella*/
        ContentValues cv = new ContentValues();
        cv.put(dataBase_string.A_FIELD_EMAIL, email);
        cv.put(dataBase_string.A_FIELD_NOMEUTENTEINPUT, nomeUtenteInput);
        cv.put(dataBase_string.A_FIELD_PASSWORD, password);
        cv.put(dataBase_string.A_FIELD_TIPOSERVERINPUT, tipoServerInput);
        cv.put(dataBase_string.A_FIELD_SERVERINPUT, serverInput);
        cv.put(dataBase_string.A_FIELD_TIPOPROTEZIONEINPUT, tipoProtezioneInput);
        cv.put(dataBase_string.A_FIELD_PORTINPUT, portInput);
        cv.put(dataBase_string.A_FIELD_SERVEROUTPUT, serverOutput);
        cv.put(dataBase_string.A_FIELD_TIPOPROTEZIONEOUTPUT, tipoProtezioneOutput);
        cv.put(dataBase_string.A_FIELD_PORTOUTPUT, portOutput);
        cv.put(dataBase_string.A_FIELD_NOMEUTENTEOUTPUT, nomeUtenteOutput);
        cv.put(dataBase_string.A_FIELD_PASSWORDOUTPUT, passwordOutput);
        cv.put(dataBase_string.A_FIELD_ULTIMOAGGIORNAMENTO, dataUltimoAggiornamento);
        try {
            return (int) db.insert(dataBase_string.TBL_ACCOUNT, null, cv);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }
        return -1;
    }
    /*
    public int salvaEmail(String mittente, String data, String oggetto_ricevuto, String testo_ricevuto){

        return 0;
    }
    */

    public Cursor recuperaTipoServer(int idTipoDiServer) {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT " + dataBase_string.TS_FIELD_DESCSERVER + " FROM " + dataBase_string.TBL_TIPOSERVERINPUT + " WHERE " +
                    dataBase_string.TS_FIELD_IDSERVERINPUT + " = '" + idTipoDiServer + "'";
            c = db.rawQuery(q, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return c;
    }


    public Cursor recuperaAccount(int idAccount) {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT * FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_IDACCOUNT + " = '" + idAccount + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return c;
    }

    public Cursor recuperaAccountbyEmail(String email) {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT * FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return c;
    }

    public String[] recuperaEmail(int id) {
        Cursor c = recuperaAccount(id);
        String password = "";
        String username = "";
        String host = "";
        int inputProtection = 0;
        String port = "";
        String protocol = "";
        while (c.moveToNext()) {
            password = c.getString(2);
            username = c.getString(4);
            host = c.getString(5);
            inputProtection = c.getInt(6);
            int portInt = c.getInt(7);
            port = Integer.toString(portInt);
            Cursor c1 = recuperaTipoServer(c.getInt(3));
            while (c1.moveToNext()) {
                protocol = c1.getString(0);

            }
            Log.d("CONFIGURAZIONE", protocol);
            Log.d("CONFIGURAZIONE", host);
            Log.d("CONFIGURAZIONE", port);
            Log.d("CONFIGURAZIONE", username);
            Log.d("CONFIGURAZIONE", password);
        }
        String[] connectionArray = new String[6];
        connectionArray[0] = protocol;
        connectionArray[1] = host;
        connectionArray[2] = port;
        connectionArray[3] = username;
        CryptoUtil cu = new CryptoUtil();
        String passwordOK = cu.simpleStringDecrypt(password);
        connectionArray[4] = passwordOK;
        connectionArray[5] = inputProtection + "";

        return connectionArray;

    }

    public Cursor recuperaDati(int id) {
        return null;
    }

    public Cursor query(String tbl_name) {
        Cursor crs = null;
        try {
            SQLiteDatabase db = database.getReadableDatabase();
            crs = db.query(tbl_name, null, null, null, null, null, null, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return crs;
    }

    /*Cancella tutto il database*/
    public int deleteAll() {
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            db.delete(dataBase_string.TBL_ACCOUNT, null, null);
            db.delete(dataBase_string.TBL_EMAILRICEVUTE, null, null);
            db.delete(dataBase_string.TBL_EMAILINVIATE, null, null);
            db.delete(dataBase_string.TBL_ALLEGATIRICEVUTI, null, null);
            db.delete(dataBase_string.TBL_ALLEGATIINVIATI, null, null);
            db.delete(dataBase_string.TBL_TIPOPROTEZIONE, null, null);
            db.delete(dataBase_string.TBL_TIPOSERVERINPUT, null, null);
            return 1;
        } catch (SQLiteException sqle) {
            return 0;
        }
    }

    public void aggiornaOutput(String email, String serverOutput, int tipoProtezioneOutput, int portOutput, String nomeUtenteOutput, String passwordOutput) {
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor c = findByEmail(dataBase_string.TBL_ACCOUNT, dataBase_string.A_FIELD_EMAIL, email);

        if (c != null) {

            ContentValues cv = new ContentValues();
            cv.put(dataBase_string.A_FIELD_SERVEROUTPUT, serverOutput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_TIPOPROTEZIONEOUTPUT, tipoProtezioneOutput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_PORTOUTPUT, portOutput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            /*cv.put(dataBase_string.A_FIELD_NOMEUTENTEOUTPUT, nomeUtenteOutput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_PASSWORDOUTPUT, passwordOutput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);*/


            c.close();
        }
    }

    public void aggiornaInput(String email, String password, String serverInput, int tipoProtezioneInput, int portInput) {
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor c = findByEmail(dataBase_string.TBL_ACCOUNT, dataBase_string.A_FIELD_EMAIL, email);

        if (c != null) {

            ContentValues cv = new ContentValues();
            cv.put(dataBase_string.A_FIELD_PASSWORD, password);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_SERVERINPUT, serverInput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_TIPOPROTEZIONEINPUT, tipoProtezioneInput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_PORTINPUT, portInput);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            cv.put(dataBase_string.A_FIELD_PASSWORDOUTPUT, password);
            db.update(dataBase_string.TBL_ACCOUNT, cv, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);

            c.close();
        }
    }

    /*Recupera l'elemento che corrisponde all'id ricercato
    * @param tbl_name: nome della tabella
    * @param id_field: nome del campo in cui si effettua la ricerca
    * @param id: id che si vuole ricercare
    */
    public Cursor findById(String tbl_name, String id_field, int id) {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getReadableDatabase();
            String q = "SELECT * FROM " + tbl_name + " WHERE " + id_field + "=" + id + ";";
            Log.i("query", q);
            c = db.rawQuery(q, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return c;
    }

    public Cursor findByEmail(String tbl_name, String email_field, String email) {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getReadableDatabase();
            String q = "SELECT * FROM " + tbl_name + " WHERE " + email_field + " = '" + email + "'";
            Log.i("query", q);
            c = db.rawQuery(q, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return c;
    }

    public String recuperaHost(String email) {
        Cursor c = null;
        String host;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT " + dataBase_string.A_FIELD_SERVEROUTPUT + " FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
            c.moveToNext();
            host = c.getString(0);
            Log.d("RISULATO", host);
        } catch (SQLiteException sqle) {
            return null;
        }
        return host;
    }

    public int recuperaPort(String email) {
        Cursor c = null;
        int port;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT " + dataBase_string.A_FIELD_PORTOUTPUT + " FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
            c.moveToNext();
            port = c.getInt(0);
            Log.d("RISULATO", String.valueOf(port));
        } catch (SQLiteException sqle) {
            return -1;
        }
        return port;
    }

    public int recuperaId(String email) {
        Cursor c = null;
        int id;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT " + dataBase_string.A_FIELD_IDACCOUNT + " FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
            c.moveToNext();
            id = c.getInt(0);
            //Log.d("RISULATO", String.valueOf(id));
        } catch (SQLiteException sqle) {
            return -1;
        }
        return id;
    }

    public String recuperaPassword(String email) {
        Cursor c = null;
        String password;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT " + dataBase_string.A_FIELD_PASSWORDOUTPUT + " FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
            c.moveToNext();
            password = c.getString(0);
            //Log.d("RISULATO", password);
        } catch (SQLiteException sqle) {
            return null;
        }
        return password;
    }

    public void saveSending(emailSendingBean ms) {
         /*Recupera un riferimento al database che ne permette anche la modifica*/
        SQLiteDatabase db = database.getWritableDatabase();
        /*Riempo la tabella*/
        ContentValues cv = new ContentValues();
        cv.put(dataBase_string.EI_FIELD_DESTINATARIO, ms.getTo());
        cv.put(dataBase_string.EI_FIELD_IDACCOUNTMITTENTE, recuperaId(ms.getFrom()));
        cv.put(dataBase_string.EI_FIELD_OGGETTOINVIATO, ms.getOggetto());
        cv.put(dataBase_string.EI_FIELD_TESTOINVIATO, ms.getCorpo());
        cv.put(dataBase_string.EI_FIELD_DATAINVIATO, ms.getDate());
        Log.d("RISULATO", ms.getTo());
        Log.d("RISULATO", String.valueOf(recuperaId(ms.getFrom())));
        Log.d("RISULATO", ms.getOggetto());
        Log.d("RISULATO", ms.getCorpo());
        Log.d("RISULATO", ms.getDate());
        try {
            db.insert(dataBase_string.TBL_EMAILINVIATE, null, cv);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }

    }

    //cancello l'account e le relative email inviate e ricevute
    public void deleteAccount(String email) {
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            int id = recuperaId(email);
            db.delete(dataBase_string.TBL_ACCOUNT, dataBase_string.A_FIELD_EMAIL + " = '" + email + "'", null);
            db.delete(dataBase_string.TBL_EMAILINVIATE, dataBase_string.EI_FIELD_IDACCOUNTMITTENTE + " = '" + id + "'", null);
            db.delete(dataBase_string.TBL_EMAILRICEVUTE, dataBase_string.ER_FIELD_IDACCOUNTRICEVENTE + " = '" + id + "'", null);

         } catch (SQLiteException sqle) {
        }
    }


    public void saveReceiving(emailReceivingBean mr, String email) {
         /*Recupera un riferimento al database che ne permette anche la modifica*/
        SQLiteDatabase db = database.getWritableDatabase();
        /*Riempo la tabella*/
        ContentValues cv = new ContentValues();
        cv.put(dataBase_string.ER_FIELD_IDACCOUNTRICEVENTE, recuperaId(email));
        cv.put(dataBase_string.ER_FIELD_MITTENTE, mr.getFrom());
        cv.put(dataBase_string.ER_FIELD_OGGETTORICEVUTO, mr.getOggetto());
        cv.put(dataBase_string.ER_FIELD_TESTORICEVUTO, mr.getCorpo());
        cv.put(dataBase_string.ER_FIELD_DATARICEVUTO, mr.getDate());
        cv.put(dataBase_string.ER_FIELD_FLAGLETTO, mr.getFlagLetto());
        /*Log.d("RISULATO a chi ", mr.getTo());
        Log.d("RISULATO id email", String.valueOf(recuperaId(email)));
        Log.d("RISULATO oggetto", mr.getOggetto());
        Log.d("RISULATO corpo", mr.getCorpo());
        Log.d("RISULATO data", mr.getDate());*/
        try {
            db.insert(dataBase_string.TBL_EMAILRICEVUTE, null, cv);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }

    }

    public String recuperaData(String email) {
        Cursor c = null;
        String date;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT " + dataBase_string.A_FIELD_ULTIMOAGGIORNAMENTO + " FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            //Log.d("QUERY",q);
            c = db.rawQuery(q, null);
            c.moveToNext();
            date = c.getString(0);
        } catch (SQLiteException sqle) {
            return null;
        }
        return date;
    }

    public void updateUltimoAggiornamento(String email, String dataAttuale) {
        SQLiteDatabase db = database.getWritableDatabase();
        /*Riempo la tabella*/
        ContentValues cv = new ContentValues();
        cv.put(dataBase_string.A_FIELD_ULTIMOAGGIORNAMENTO, dataAttuale);
        String where = "email" + " = '" + email + "'";
        try {
            db.update(dataBase_string.TBL_ACCOUNT, cv, where, null);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }
    }

    public email[] recuperaMailRicevute(int id) {
        Cursor c = null;

        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT * FROM " + dataBase_string.TBL_EMAILRICEVUTE + " WHERE " + dataBase_string.ER_FIELD_IDACCOUNTRICEVENTE + " = '" + id + "'ORDER BY "+dataBase_string.ER_FIELD_DATARICEVUTO
                    +" DESC";
            Log.d("QUERY",q);
            c = db.rawQuery(q, null);
            email[] elenco = new email[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                elenco[i] = new email(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5),c.getInt(6));
                /*String[] emailArray = new String[6];
                emailArray[0] = String.valueOf(c.getInt(0));
                emailArray[1] = String.valueOf(c.getInt(1));
                emailArray[2] = c.getString(2);
                emailArray[3] = c.getString(3);
                emailArray[4] = c.getString(4);
                emailArray[5] = c.getString(5);*/
                i++;
            }

            return elenco;
        } catch (SQLiteException sqle) {
            return null;
        }
    }

    public void updateFlag(int idEmail) {
    /*Recupera un riferimento al database che ne permette anche la modifica*/
        SQLiteDatabase db = database.getWritableDatabase();
        /*Riempo la tabella*/
        ContentValues cv = new ContentValues();
        cv.put(dataBase_string.ER_FIELD_FLAGLETTO, 1);
        try {
            db.update(dataBase_string.TBL_EMAILRICEVUTE, cv, dataBase_string.ER_FIELD_IDEMAILRICEVUTE + " = '" + idEmail + "'", null);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }

    }

    public emailSend[] recuperaMailInviate(int idAccount) {
        Cursor c = null;

        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT * FROM " + dataBase_string.TBL_EMAILINVIATE + " WHERE " + dataBase_string.EI_FIELD_IDACCOUNTMITTENTE + " = '" + idAccount + "'";
            Log.d("QUERY",q);
            c = db.rawQuery(q, null);
            emailSend[] elenco = new emailSend[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                elenco[i] = new emailSend(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
                i++;
            }

            return elenco;
        } catch (SQLiteException sqle) {
            return null;
        }
    }

    public void deleteReceivedEmail(int idEmail) {
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            db.delete(dataBase_string.TBL_EMAILRICEVUTE, dataBase_string.ER_FIELD_IDEMAILRICEVUTE + " = '" + idEmail + "'", null);
        } catch (SQLiteException sqle) {
        }
    }

    public void deleteSendedEmail(int idEmail) {
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            db.delete(dataBase_string.TBL_EMAILINVIATE, dataBase_string.EI_FIELD_IDEMAILINVIATE + " = '" + idEmail + "'", null);
        } catch (SQLiteException sqle) {
        }
    }

    //metodo che verifica se un username  gia esistente
    public boolean existEmail(String email) {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT * FROM " + dataBase_string.TBL_ACCOUNT + " WHERE " + dataBase_string.A_FIELD_EMAIL + " = '" + email + "'";
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
            if (c.moveToNext() == false) {
                return false;
            } else {
                return true;
            }
        } catch (SQLiteException sqle) {
            return false;
        }
    }

    public int numberOfAccount() {
        Cursor c = null;
        try {
            SQLiteDatabase db = database.getWritableDatabase();
            String q = "SELECT COUNT(*) FROM " + dataBase_string.TBL_ACCOUNT;
            Log.d("QUERY", q);
            c = db.rawQuery(q, null);
            c.moveToNext();
            int numberOfAccount = c.getInt(0);
            return numberOfAccount;
        } catch (SQLiteException sqle) {
            return -1;
        }
    }


}

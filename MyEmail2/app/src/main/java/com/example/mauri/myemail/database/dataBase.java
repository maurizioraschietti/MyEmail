package com.example.mauri.myemail.database;

/**
 * Created by Mauri on 02/08/2015.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mauri.myemail.model.tipoDiProtezione;
import com.example.mauri.myemail.model.tipoServerInput;

/**
 * Created by Mauri on 02/08/2015.
 *
 * Classe per la creazione del database usato dall'applicazione.
 * Crea una tabella
 * Account: tabella in cui saranno contenuti i dati di configurazione relativi all'account creato
 *
 */
public class dataBase extends SQLiteOpenHelper {

    private dataBase database;

    /*Costruttore*/
    public dataBase(Context context) {
        super(context, dataBase_string.DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*Creo la tabella per l'account*/
        String q = "CREATE TABLE " + dataBase_string.TBL_ACCOUNT +
                " ( " + dataBase_string.A_FIELD_IDACCOUNT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.A_FIELD_EMAIL + " TEXT not null," +
                dataBase_string.A_FIELD_PASSWORD + " TEXT not null," +
                dataBase_string.A_FIELD_TIPOSERVERINPUT + " INT not null," +
                dataBase_string.A_FIELD_NOMEUTENTEINPUT + " TEXT not null," +
                dataBase_string.A_FIELD_SERVERINPUT + " TEXT not null," +
                dataBase_string.A_FIELD_TIPOPROTEZIONEINPUT + " INT not null," +
                dataBase_string.A_FIELD_PORTINPUT + " INT not null," +
                dataBase_string.A_FIELD_SERVEROUTPUT + " TEXT not null," +
                dataBase_string.A_FIELD_TIPOPROTEZIONEOUTPUT + " INT  not null," +
                dataBase_string.A_FIELD_PORTOUTPUT + " INT not null,"+
                dataBase_string.A_FIELD_NOMEUTENTEOUTPUT + " TEXT," +
                dataBase_string.A_FIELD_PASSWORDOUTPUT + " TEXT," +
                dataBase_string.A_FIELD_ULTIMOAGGIORNAMENTO + " TEXT,"
                + " FOREIGN KEY (" + dataBase_string.A_FIELD_TIPOSERVERINPUT + ") REFERENCES " + dataBase_string.TBL_TIPOSERVERINPUT + " (" + dataBase_string.TS_FIELD_IDSERVERINPUT + "),"
                + " FOREIGN KEY (" + dataBase_string.A_FIELD_TIPOPROTEZIONEINPUT + ") REFERENCES " + dataBase_string.TBL_TIPOPROTEZIONE + " (" + dataBase_string.TP_FIELD_IDPROTEZIONE + "),"
                + " FOREIGN KEY (" + dataBase_string.A_FIELD_TIPOPROTEZIONEOUTPUT + ") REFERENCES " + dataBase_string.TBL_TIPOPROTEZIONE + " (" + dataBase_string.TP_FIELD_IDPROTEZIONE + "));";
        db.execSQL(q);

        /*Creo la tabella per messaggi ricevuti*/
        q = "CREATE TABLE " + dataBase_string.TBL_EMAILRICEVUTE +
                " ( " + dataBase_string.ER_FIELD_IDEMAILRICEVUTE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.ER_FIELD_IDACCOUNTRICEVENTE + " INT not null," +
                dataBase_string.ER_FIELD_MITTENTE + " TEXT not null," +
                dataBase_string.ER_FIELD_DATARICEVUTO + " DATE not null," +
                dataBase_string.ER_FIELD_OGGETTORICEVUTO + " TEXT," +
                dataBase_string.ER_FIELD_TESTORICEVUTO + " TEXT," +
                dataBase_string.ER_FIELD_FLAGLETTO + " INTEGER not null,"
                + " FOREIGN KEY (" + dataBase_string.ER_FIELD_IDACCOUNTRICEVENTE + ") REFERENCES " + dataBase_string.TBL_ACCOUNT + " (" + dataBase_string.A_FIELD_IDACCOUNT + "));";
        db.execSQL(q);

        /*Creo la tabella per messaggi inviati*/
        q = "CREATE TABLE " + dataBase_string.TBL_EMAILINVIATE +
                " ( " + dataBase_string.EI_FIELD_IDEMAILINVIATE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.EI_FIELD_IDACCOUNTMITTENTE + " INT not null," +
                dataBase_string.EI_FIELD_DESTINATARIO + " TEXT not null," +
                dataBase_string.EI_FIELD_DATAINVIATO + " DATE not null," +
                dataBase_string.EI_FIELD_OGGETTOINVIATO + " TEXT," +
                dataBase_string.EI_FIELD_TESTOINVIATO + " TEXT,"
                + " FOREIGN KEY (" + dataBase_string.EI_FIELD_IDACCOUNTMITTENTE + ") REFERENCES " + dataBase_string.TBL_ACCOUNT + " (" + dataBase_string.A_FIELD_IDACCOUNT + "));";
        db.execSQL(q);

        /*Creo la tabella per gli allegati ricevuti*/
        q = "CREATE TABLE " + dataBase_string.TBL_ALLEGATIRICEVUTI +
                " ( " + dataBase_string.AR_FIELD_IDALLEGATORICEVUTO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.AR_FIELD_IDEMAILRICEVUTA+ " INT not null," +
                dataBase_string.AR_FIELD_PATHALLEGATORICEVUTO+ " TEXT not null,"
                + " FOREIGN KEY (" + dataBase_string.AR_FIELD_IDEMAILRICEVUTA + ") REFERENCES " + dataBase_string.TBL_EMAILRICEVUTE + " (" + dataBase_string.ER_FIELD_IDEMAILRICEVUTE + "));";
        db.execSQL(q);

        /*Creo la tabella per gli allegati inviati*/
        q = "CREATE TABLE " + dataBase_string.TBL_ALLEGATIINVIATI +
                " ( " + dataBase_string.AI_FIELD_IDALLEGATOINVIATO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.AI_FIELD_IDEMAILINVIATA+ " INT not null," +
                dataBase_string.AI_FIELD_PATHALLEGATOINVIATO+ " TEXT not null,"
                + " FOREIGN KEY (" + dataBase_string.AI_FIELD_IDEMAILINVIATA + ") REFERENCES " + dataBase_string.TBL_EMAILINVIATE + " (" + dataBase_string.EI_FIELD_IDEMAILINVIATE + "));";
        db.execSQL(q);

        /*Creo la tabella per i tipi di protezione*/
        q = "CREATE TABLE " + dataBase_string.TBL_TIPOPROTEZIONE +
                " ( " + dataBase_string.TP_FIELD_IDPROTEZIONE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.TP_FIELD_DESCPROT + " TEXT not null)";
        db.execSQL(q);

        /*Creo la tabella per i tipi di server in ingresso*/
        q = "CREATE TABLE " + dataBase_string.TBL_TIPOSERVERINPUT +
                " ( " + dataBase_string.TS_FIELD_IDSERVERINPUT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dataBase_string.TS_FIELD_DESCSERVER + " TEXT not null)";
        db.execSQL(q);

        popolazioneIniziale(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {  }

    public void popolazioneIniziale(SQLiteDatabase db) {
        int size = 5;
        tipoDiProtezione[] tp = new tipoDiProtezione[size];
        tp[0] = new tipoDiProtezione(0, "Nessuna");
        tp[1] = new tipoDiProtezione(1, "SSL");
        tp[2] = new tipoDiProtezione(2, "SSL (Tutti i certificati)");
        tp[3] = new tipoDiProtezione(3, "TLS");
        tp[4] = new tipoDiProtezione(4, "TLS (Tutti i certificati)");

        for (int i = 0; i < tp.length; i++) {
            /*Riempo la tabella*/
            ContentValues cv = new ContentValues();
            cv.put(dataBase_string.TP_FIELD_IDPROTEZIONE, tp[i].getIdTipoDiProtezione());
            cv.put(dataBase_string.TP_FIELD_DESCPROT, tp[i].getDescTipoDiProtezione());
            try {
                db.insert(dataBase_string.TBL_TIPOPROTEZIONE, null, cv);
            } catch (SQLiteException sqle) {
                sqle.printStackTrace();
            }
        }

        int size1 = 2;
        tipoServerInput[] ts = new tipoServerInput[size1];
        ts[0] = new tipoServerInput(0, "pop3");
        ts[1] = new tipoServerInput(1, "imap");

        for (int i = 0; i < ts.length; i++) {
            /*Riempo la tabella*/
            ContentValues cv = new ContentValues();
            cv.put(dataBase_string.TS_FIELD_IDSERVERINPUT, ts[i].getIdTipoDiServer());
            cv.put(dataBase_string.TS_FIELD_DESCSERVER, ts[i].getDescTipoDiServer());
            try {
                db.insert(dataBase_string.TBL_TIPOSERVERINPUT, null, cv);
            } catch (SQLiteException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    public void modificaAccount(int id) {
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor c = findById(dataBase_string.TBL_ACCOUNT,dataBase_string.A_FIELD_IDACCOUNT,id);


    }

    public Cursor findById(String tbl_name, String id_field, int id){
        Cursor c = null;
        try{
            SQLiteDatabase db = database.getReadableDatabase();
            String q = "SELECT * FROM "+ tbl_name + " WHERE " + id_field + "=" + id +";";
            Log.i("query", q);
            c = db.rawQuery(q, null);
        }
        catch(SQLiteException sqle){
            return null;
        }
        return c;
    }
}
package com.example.mauri.myemail.database;

/**
 * Created by Mauri on 02/08/2015.
 */
public class dataBase_string {

    public static final String DBNAME = "myemail.db";
    /* --- TABELLA ACCOUNT --- */
    public static final String TBL_ACCOUNT = "account";
    public static final String A_FIELD_IDACCOUNT = "_id";
    public static final String A_FIELD_EMAIL = "email";
    public static final String A_FIELD_PASSWORD = "password";
    public static final String A_FIELD_TIPOSERVERINPUT = "tipo_server_input";
    public static final String A_FIELD_NOMEUTENTEINPUT = "nome_utente_input";
    public static final String A_FIELD_SERVERINPUT = "server_input";
    public static final String A_FIELD_TIPOPROTEZIONEINPUT = "tipo_protezione_input";
    public static final String A_FIELD_PORTINPUT = "port_input";
    public static final String A_FIELD_SERVEROUTPUT = "server_output";
    public static final String A_FIELD_TIPOPROTEZIONEOUTPUT = "tipo_protezione_output";
    public static final String A_FIELD_PORTOUTPUT= "port_output";
    public static final String A_FIELD_NOMEUTENTEOUTPUT = "nome_utente_output";
    public static final String A_FIELD_PASSWORDOUTPUT = "password_output";
    public static final String A_FIELD_ULTIMOAGGIORNAMENTO = "ultimo_aggiornamento";


    /* --- TABELLA MESSAGGI RICEVUTI --- */
    public static final String TBL_EMAILRICEVUTE = "email_ricevute";
    public static final String ER_FIELD_IDEMAILRICEVUTE = "_id";
    public static final String ER_FIELD_IDACCOUNTRICEVENTE = "id_account_ricevente";
    public static final String ER_FIELD_MITTENTE = "mittente";
    public static final String ER_FIELD_DATARICEVUTO = "data_ricevuto";
    public static final String ER_FIELD_OGGETTORICEVUTO = "oggetto_ricevuto";
    public static final String ER_FIELD_TESTORICEVUTO = "testo_ricevuto";
    public static final String ER_FIELD_FLAGLETTO = "flag_letto";


    /* --- TABELLA MESSAGGI INVITATI --- */
    public static final String TBL_EMAILINVIATE = "email_inviate";
    public static final String EI_FIELD_IDEMAILINVIATE = "_id";
    public static final String EI_FIELD_IDACCOUNTMITTENTE = "id_account_mittente";
    public static final String EI_FIELD_DESTINATARIO = "destinatario";
    public static final String EI_FIELD_DATAINVIATO = "data_inviato";
    public static final String EI_FIELD_OGGETTOINVIATO = "oggetto_inviato";
    public static final String EI_FIELD_TESTOINVIATO = "testo_inviato";


    /* --- TABELLA ALLEGATI RICEVUTI --- */
    public static final String TBL_ALLEGATIRICEVUTI = "allegati_ricevuti";
    public static final String AR_FIELD_IDALLEGATORICEVUTO= "_id";
    public static final String AR_FIELD_IDEMAILRICEVUTA = "email_ricevuta";
    public static final String AR_FIELD_PATHALLEGATORICEVUTO = "path_allegato_ricevuto";

    /* --- TABELLA ALLEGATI INVIATI--- */
    public static final String TBL_ALLEGATIINVIATI = "allegati_inviati";
    public static final String AI_FIELD_IDALLEGATOINVIATO= "_id";
    public static final String AI_FIELD_IDEMAILINVIATA = "email_inviata";
    public static final String AI_FIELD_PATHALLEGATOINVIATO = "path_allegato_inviato";


    /* --- TABELLA TIPO DI PROTEZIONE --- */
    public static final String TBL_TIPOPROTEZIONE = "tipo_protezione";
    public static final String TP_FIELD_IDPROTEZIONE = "_id";
    public static final String TP_FIELD_DESCPROT = "desc";


    /* --- TABELLA TIPO DI SERVER IN INGRESSO --- */
    public static final String TBL_TIPOSERVERINPUT = "tipo_server_input";
    public static final String TS_FIELD_IDSERVERINPUT = "_id";
    public static final String TS_FIELD_DESCSERVER = "desc";

}
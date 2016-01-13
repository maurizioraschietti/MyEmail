package com.example.mauri.myemail.model;

/**
 * Created by Mauri on 06/12/2015.
 */
public class email {

    int id;
    int to;
    private String from;
    private String data;
    private String oggetto;
    private String testo;
    private int flag;

    public email(int id, int to, String from, String data, String oggetto, String testo, int flag) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.data = data;
        this.oggetto = oggetto;
        this.testo = testo;
        this.flag= flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;

    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }





}

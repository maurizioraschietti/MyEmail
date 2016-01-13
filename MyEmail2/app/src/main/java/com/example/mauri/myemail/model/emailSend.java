package com.example.mauri.myemail.model;

/**
 * Created by Mauri on 13/12/2015.
 */
public class emailSend {

    int id;
    int from;
    private String to;
    private String data;
    private String oggetto;
    private String testo;

    public emailSend(int id, int from, String to, String data, String oggetto, String testo) {
        this.id = id;
        this.to = to;

        this.from = from;
        this.data = data;
        this.oggetto = oggetto;
        this.testo = testo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;

    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
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
}

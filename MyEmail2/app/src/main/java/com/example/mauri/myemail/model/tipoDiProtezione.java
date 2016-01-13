package com.example.mauri.myemail.model;

/**
 * Created by Mauri on 11/08/2015.
 */
public class tipoDiProtezione {

    int idTipoDiProtezione;
    String descTipoDiProtezione;

    public tipoDiProtezione(int id, String desc){
        this.idTipoDiProtezione = id;
        this.descTipoDiProtezione = desc;
    }

    public int getId() {
        return idTipoDiProtezione;
    }

    public String getDesc() {
        return descTipoDiProtezione;
    }
    @Override
    public String toString() {
        return  descTipoDiProtezione;
    }

    public int getIdTipoDiProtezione() {
        return idTipoDiProtezione;
    }

    public void setIdTipoDiProtezione(int idTipoDiProtezione) {
        this.idTipoDiProtezione = idTipoDiProtezione;
    }

    public String getDescTipoDiProtezione() {
        return descTipoDiProtezione;
    }

    public void setDescTipoDiProtezione(String descTipoDiProtezione) {
        this.descTipoDiProtezione = descTipoDiProtezione;
    }
}


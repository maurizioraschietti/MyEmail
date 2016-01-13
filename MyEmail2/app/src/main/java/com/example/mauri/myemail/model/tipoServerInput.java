package com.example.mauri.myemail.model;

/**
 * Created by Mauri on 11/08/2015.
 */
public class tipoServerInput {

    int idTipoDiServer;
    String descTipoDiServer;

    public int getIdTipoDiServer() {
        return idTipoDiServer;
    }

    public void setIdTipoDiServer(int idTipoDiServer) {
        this.idTipoDiServer = idTipoDiServer;
    }

    public String getDescTipoDiServer() {
        return descTipoDiServer;
    }

    public void setDescTipoDiServer(String descTipoDiServer) {
        this.descTipoDiServer = descTipoDiServer;
    }

    public tipoServerInput(int id, String desc){
        this.idTipoDiServer = id;
        this.descTipoDiServer = desc;
    }

    public int getId() {
        return idTipoDiServer;
    }

    public String getDesc() {
        return descTipoDiServer;
    }
    @Override
    public String toString() {
        return  descTipoDiServer;
    }

}



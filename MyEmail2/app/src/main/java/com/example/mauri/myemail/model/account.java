package com.example.mauri.myemail.model;

/**
 * Created by Mauri on 13/08/2015.
 */
public class account {
    int id;
    String email;
    String nomeUtenteInput;
    String password;
    int tipoServerInput;
    String serverInput;
    int tipoProtezioneInput;
    int portInput;
    String serverOutput;
    int tipoProtezioneOutput;
    int portOutput;
    String nomeUtenteOutput;
    String passwordOutput;

    public account(int id, String email, String nomeUtenteInput, String password, int tipoServerInput, String serverInput, int tipoProtezioneInput,
                   int portInput, String serverOutput, int tipoProtezioneOutput, int portOutput, String nomeUtenteOutput, String passwordOutput){
        this.id = id;
        this.email = email;
        this.nomeUtenteInput = nomeUtenteInput;
        this.password = password;
        this.tipoServerInput = tipoServerInput;
        this.serverInput = serverInput;
        this.tipoProtezioneInput = tipoProtezioneInput;
        this.portInput = portInput;
        this.serverOutput = serverOutput;
        this.tipoProtezioneOutput = tipoProtezioneOutput;
        this.portOutput = portOutput;
        this.nomeUtenteOutput = nomeUtenteOutput;
        this.passwordOutput = passwordOutput;

    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return email;
    }
    @Override
    public String toString() {
        return  email;
    }
}

package com.example.mauri.myemail.model;

 /**
 * Created by Mauri on 13/12/2015.
 */
public class emailSendingBean {
	// Proprietà utili alla mail
	private String from;
	private String to;
	private String oggetto;
	private String testo;
	private String data;

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
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
	public String getCorpo() {
		return testo;
	}
	public void setCorpo(String corpo) {
		this.testo = corpo;
	}
	public void setDate(String data) {
		this.data = data;
	}
	public String getDate() {
		return data;
	}
}

package Server;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDate;

public class Noticia implements Serializable{
	
	//private static int id = 0;
	private char[] carateres;
	private String topico;
	private Date data;
	private String produtor;
	
	public Noticia(String topico, String produtor) {
		this.topico = topico;
		this.produtor = produtor;
		this.carateres = new char[180];
		this.data = new Date();
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getProdutor() {
		return produtor;
	}

	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}

	public String getTopico() {
		return topico;
	}

	public void setTopico(String topico) {
		this.topico = topico;
	}

	public char[] getCarateres() {
		return carateres;
	}

	public void setCarateres(char[] carateres) {
		this.carateres = carateres;
	}
	
	public String toString() {
		return "-----------------------------------------------------------\n"
				+ "Notícia -> Tópico: " + this.topico + "\n" 
				+ "Escrita por: " + this.produtor + " -> " + data + "\n" 
				+ "\n" + carateres.toString();
	}
}

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
	
	public Noticia() {
		//Noticia.id = Noticia.id++;
		this.carateres = new char[180];
		this.data = new Date();
	}
	
	public Noticia(String topico, String produtor) {
		//Noticia.id = Noticia.id++;
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

	/*public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Noticia.id = id;
	}*/

	public char[] getCarateres() {
		return carateres;
	}

	public void setCarateres(char[] carateres) {
		this.carateres = carateres;
	}
	
	//falta data
	public String toString() {
		return "string";
	}
}

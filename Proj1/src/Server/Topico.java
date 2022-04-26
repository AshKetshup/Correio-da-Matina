package Server;

import java.io.Serializable;

public class Topico implements Serializable{
	private String topico;
	
	public Topico() {}
	
	public Topico(String topico) {
		this.topico = topico;
	}

	public String getTopico() {
		return topico;
	}

	public void setTopico(String topico) {
		this.topico = topico;
	}
	
	public String toString() {
		return this.topico;
	}

}

package Server;

import java.util.ArrayList;

public interface Servidor_noticias_interface extends java.rmi.Remote{
	
	//consultar noticias todas
	public ArrayList<Noticia> consultar() throws java.rmi.RemoteException;
	
	//consultar noticias de certo tópico
	public ArrayList<Noticia> consultar_topic(String topico) throws java.rmi.RemoteException;
	
	//criar nova noticia
	public void criar(String topico, String produtor, char[] carateres) throws java.rmi.RemoteException;
	
	//adicionar novas noticias ao conjunto de noticias
	public void adicionar(Noticia n) throws java.rmi.RemoteException;
	
	//subscrever tópicos de noticias
	//substituir pelos callbacks
	public void notificar() throws java.rmi.RemoteException;
	
	//armazenar noticias no backup
	public ArrayList<Noticia> backup() throws java.rmi.RemoteException;
	

}

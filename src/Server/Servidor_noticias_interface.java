package Server;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import Client.Subscrition_listener;

public interface Servidor_noticias_interface extends java.rmi.Remote{
	
	//open the file containing login info
	public ArrayList<login> open_login_file() throws java.rmi.RemoteException;
	
	//update the file containing the login info
	public void update_login_file(login l) throws java.rmi.RemoteException;
	
	//iniciar login
	public boolean user_login(String username, String password) throws java.rmi.RemoteException;
	
	//public String compare_pwd(String password, byte[] salt) throws java.rmi.RemoteException;
	
	//registar 
	public void register_user(String username, String password, String confirm_pwd, int resgistered_publisher) throws java.rmi.RemoteException;
	
	
	
	
	//abrir os ficheiros com as noticias no servidor RMI
	public void abrir_ficheiros() throws java.rmi.RemoteException;
	
	//atualizar os ficheiros das noticias no servidor RMI
	public void atualizar_ficheiros() throws java.rmi.RemoteException;
	
	//consultar noticias todas
	public ArrayList<Noticia> consultar() throws java.rmi.RemoteException;
	
	//consultar noticias de certo tópico
	public ArrayList<Noticia> consultar_topic() throws java.rmi.RemoteException;
	
	//adicionar novas noticias ao conjunto de noticias
	public void adicionar(Noticia n) throws java.rmi.RemoteException;
		
		
	
	
	//criar novo topico
	public boolean criar_topico() throws java.rmi.RemoteException;
	
	//abrir file topico
	public void abrir_fich_topico() throws java.rmi.RemoteException;
	
	//criar nova noticia
	public void criar_noticia(String produtor) throws java.rmi.RemoteException;
	
	//criar texto da noticia
	public char[] texto_noticia() throws java.rmi.RemoteException;
	
	
	
	
	//subscrever tópicos de noticias
	public void subscribe_news(Subscrition_listener sl) throws java.rmi.RemoteException;
	
	//public String get
	
	
	//armazenar noticias no server backup
	public ArrayList<Noticia> backup() throws java.rmi.RemoteException;
	

}

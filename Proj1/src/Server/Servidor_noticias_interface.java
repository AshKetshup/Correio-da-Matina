package Server;

import java.util.ArrayList;

public interface Servidor_noticias_interface extends java.rmi.Remote{
	
	//abrir_login_file
	public ArrayList<login> abrir_login() throws java.rmi.RemoteException;
	
	//iniciar login
	public boolean user_login(String username, String password) throws java.rmi.RemoteException;
	
	//registar 
	public void registar_user(String username, String password, String confirm_pwd) throws java.rmi.RemoteException;
	
	
	
	
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
	//substituir pelos callbacks
	public void notificar() throws java.rmi.RemoteException;
	
	
	
	//armazenar noticias no server backup
	public ArrayList<Noticia> backup() throws java.rmi.RemoteException;
	

}

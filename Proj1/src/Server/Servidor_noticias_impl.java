package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import myInputs.Read;


public class Servidor_noticias_impl extends UnicastRemoteObject implements Servidor_noticias_interface{
	ArrayList<Noticia> noticias;
	Noticia n;
	
	
	int top_size;
	String[] top = new String[top_size];
	
	public Servidor_noticias_impl() throws java.rmi.RemoteException { 
		super();
		noticias = new ArrayList<Noticia>();
	}

//---------------- ARRAYLIST<NOTICIAS> ----------------	
	
	@Override
	public void abrir_ficheiros() throws RemoteException {
		String filename;
		
		for(int i=0; i<top_size;i++) {
			filename = top[i] + ".txt";
			
			File f = new File(filename);
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream oIs = new ObjectInputStream(fis);
				
				//colocar noticas no ArrayList para serem manipuladas
				while(oIs.read()!=(-1)){
					noticias.add((Noticia)oIs.readObject());
				}
				
				oIs.close();
			}catch(IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		
		//return noticias;
	}

	@Override
	public void atualizar_ficheiros() throws RemoteException {
		String filename;
		
		//TERMINAR!!!
		
		for(int i=0; i<top_size;i++) {
			filename = top[i] + ".txt";
			
			File f = new File(filename);
			try {
				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream oOs = new ObjectOutputStream(fos);
				
				//oOs.writeObject(oo1);
				
				oOs.close();
				
				//colocar noticas no ArrayList para serem manipuladas
				//while(oIs.read()!=(-1)){
					//noticias.add((Noticia)oIs.readObject());
				//}
				
				//oIs.close();
			}catch(IOException e){// | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		
		//return noticias;
	}
	
	@Override
	public ArrayList<Noticia> consultar() throws RemoteException {
		abrir_ficheiros();
		return noticias;
	}

	@Override
	public void adicionar(Noticia n) throws RemoteException {
		noticias.add(n);
		atualizar_ficheiros();
	}
	
	@Override
	public ArrayList<Noticia> consultar_topic() throws RemoteException {
		
		//declara????o t??pico que tem que vai ser a noticia
		String topico = null;
		
		
		ArrayList<Noticia> aux = new ArrayList<Noticia>();
		System.out.println("Escolha o t??pico da sua noticia dos seguintes apresentados: \n"); 
		for(int i=0; i<top_size; i++) {
			System.out.println((i+1) + " - " + top[i]);
		}
		
		//escolha do topico
		do {
			int e = Read.aInt();
			for(int i=0; i<top_size; i++) {
				if(i == (e-1)) {
					topico = top[i];
				}
			}
		}while(topico.equals(null));
		
		String filename = topico + ".txt";
		
		File f = new File(filename);
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream oIs = new ObjectInputStream(fis);
			
			//colocar noticas no ArrayList para serem manipuladas
			while(oIs.read()!=(-1)){
				aux.add((Noticia)oIs.readObject());
			}
			
			oIs.close();
		}catch(IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		return aux;
	}

	
//---------------- NOTICIA -----------------------
	@Override
	public boolean criar_topico() throws RemoteException {
		//boolean existe = true;
		
		String topico = Read.aString();
		int i=0;
		
		//verificar se o topico j?? existe e adicionar caso n??o 
		if(top[i].isEmpty()) {
			top[i] = topico;
			top_size = i+1;
			return true;
		
		} else {
			for(i=0; i<top.length; i++) {
				if(top[i].contains(topico)) {
					System.out.println("T??pico j?? existe, re-introduza o topico");
					
					topico = Read.aString();
					
					//reinicia a verifica????os
					i=0;
				}
			}
		}
			
		top[i]=topico;
		top_size = i+1;
		return true;
		
	}
	

	@Override
	public void abrir_fich_topico() throws RemoteException {
		File t = new File("topicos.txt");
		
		int count=0;
		try {
			FileInputStream fis = new FileInputStream(t);
			ObjectInputStream oIs = new ObjectInputStream(fis);
			
			//colocar topicos ja disponiveis no list
			while(oIs.read()!=(-1)){
				top[count] = (String)oIs.readObject();
				count++;
			}
			
			//tamanho da lista
			top_size = count;
			
			oIs.close();
		}catch(IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//return top;
	}

	
	@Override
	public void criar_noticia(String produtor) throws RemoteException {
		
		abrir_fich_topico();
		if(top.length == 0) {
			System.out.println("N??o existem topicos criados, por favor crie o primeiro t??pico:\n");
			criar_topico();
		}else {
			System.out.println("Escolha o t??pico da sua noticia dos seguintes apresentados: \n"); 
			for(int i=0; i<top_size; i++) {
				System.out.println((i+1) + " - " + top[i]);
			}
		}
		
		//declara????o t??pico que tem que vai ser a noticia
		String topico = null;
		
		//declara????o da variav??l que vai conter texto da noticia
		char[] carateres;
		
		//escolha do topico
		do {
			int e = Read.aInt();
			for(int i=0; i<top_size; i++) {
				if(i == (e-1)) {
					topico = top[i];
				}
			}
		}while(topico.equals(null));
		
		carateres = texto_noticia();
		
		n = new Noticia(topico, produtor);
		n.setCarateres(carateres);
		
	}
	
	@Override
	public char[] texto_noticia() throws RemoteException {
		char[] carateres = new char[180];
		
		System.out.println("Introduza o texto da sua noticia. Tenha aten????o para n??o ultrapassar o limite de 180 carateres, que inclui os espa??os");
		
		
		//TERMINAR
		//como delimitar ou terminar a introdu????o da noticia????
		carateres = (Read.aString()).toCharArray();
		
		return null;
	}

//----------- NOTIFICA????O DE PUBLICA????O DE NOTICIA A PESSOAS SUBSCRITAS A UM CERTO T??PICO
	
	
	//TERMINAR
	//callback -> investigar isso!!!!
	@Override
	public void notificar() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

//------------------- BACKUP ------------------- 		
	
	
				
	//deve ser automatico a forma como o backup/forma como a transi????o das noticias acontece!!
	@Override
	public ArrayList<Noticia> backup() throws RemoteException {
		
		
		return null;
	}
	
//-------------- LOGIN -----------------------
	public ArrayList<login> abrir_login(){
		return null;
	}
	
	
	@Override
	public boolean user_login(String username, String password) throws RemoteException {
		
		return false;
	}

	@Override
	public void registar_user(String username, String password, String confirm_pwd) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
	
	
	


}
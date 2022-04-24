package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import Server.Servidor_noticias_impl;
import Server.Servidor_noticias_interface;

public class Servidor_noticias {
	
	public Servidor_noticias() {
		//Criar um gestor de segurança e instancia-lo
		System.setSecurityManager(new SecurityManager()); 
		
		try { 
			// Iniciar a execução do registry no porto desejado
			java.rmi.registry.LocateRegistry.createRegistry(1099); 
			System.out.println("RMI registry ready."); //possivelmente eliminar ou substituir isto
		
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
		}
		try {
			// instanciar objeto remoto
			Servidor_noticias_interface objRemoto = new Servidor_noticias_impl();
			
			//registar o objeto remoto no Registry 
			Naming.rebind("CorreioDaMatina", objRemoto); 
			System.out.println("Remote object ready");
			
		} catch (MalformedURLException | RemoteException e) {
			System.out.println(e.getMessage()); 
		}
	}
	public static void main(String[] args) {
		Servidor_noticias sn = new Servidor_noticias();
	}

}

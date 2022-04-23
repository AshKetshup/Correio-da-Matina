package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class Servidor_noticias_impl extends UnicastRemoteObject implements Servidor_noticias_interface{
	ArrayList<Noticia> noticias;
	Noticia n;
	
	public Servidor_noticias_impl() throws java.rmi.RemoteException { 
		super();
		noticias = new ArrayList<Noticia>();
	}

	@Override
	public ArrayList<Noticia> consultar() throws RemoteException {
		return noticias;
	}

	@Override
	public void adicionar(Noticia n) throws RemoteException {
		noticias.add(n);
	}

	//callback -> investigar isso!!!!
	@Override
	public void notificar() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	//deve ser automatico a forma como o backup/forma como a transição das noticias acontece!!
	@Override
	public ArrayList<Noticia> backup() throws RemoteException {
		
		/*//noticias podem estar desordenadas!!!
		//wrong
		
		//ArrayList auxiliar
		ArrayList<Noticia> aux = new ArrayList<Noticia>();
		
		//lista para guardar os diferentes tópicos
		int a=1;
		String[] topico = new String[a];
		
		
		//percorre a lista das noticias para ver os tópicos existentes
		for(int i=0; i<noticias.size(); i++) {
			//percorre a lista dos topicos para ver se o topico ká está na lista
			for(int j=0; j<a; j++) {
				if(noticias.get(i).getTopico().contains(topico[j]) == false) {
					a--;
					topico[a] = noticias.get(i).getTopico();
					a++;
				}
			}
		}
		
		//contar quantas noticias por tópico existem!!
		int count = 0;
		for(int j=0; j<a; j++) {
			for(int i=0; i<noticias.size(); i++) {
				if(noticias.get(i).getTopico().equals(topico[j])) {
					count++;
					if(count == 10) {
						//manda para o servidor de backup/ou escreve no backup
						//elimina no ficheiro atual 
						//ajusta tamanho das noticias - array para não haver espaço desperdiçado
					}
				}
			}
		}*/
		return null;
	}

	@Override
	public ArrayList<Noticia> consultar_topic(String topico) throws RemoteException {
		//wrong as well
		ArrayList<Noticia> aux = new ArrayList<Noticia>();
		for(int i=0; i<noticias.size(); i++) {
			if(noticias.get(i).getTopico().equals(topico)) {
				aux.add(noticias.get(i));
			}
		}
		return aux;
	}

	@Override
	public void criar(String topico, String produtor, char[] carateres) throws RemoteException {
		n = new Noticia(topico, produtor);
		n.setCarateres(carateres);
		
	}


}

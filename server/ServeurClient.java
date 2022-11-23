package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurClient extends Thread{
	
	private int secretNumber;
	private int clientNumber;
	private Boolean fin = false;
	
	public static void main(String[] args) {
		
		new ServeurClient().start();
	}
	
	@SuppressWarnings("resource")
	public void run() {
		
		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("Demarrage du serveur.........");
			secretNumber = new Random().nextInt(1000);
			System.out.println("Le nombre secret est " + secretNumber);
			while(true) {
				Socket socket = ss.accept();
				new Client(socket, clientNumber).start();
				++clientNumber;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	class Client extends Thread{
		
		private Socket socket;
		private int numeroClient;
		
		public Client(Socket socket, int numeroClient) {
			this.socket = socket;
			this.numeroClient = numeroClient;
		}
		
		public void run() {
			//BufferedReader lit les lignes après il envoie ça
			//InputStreamReader transforme ça en tableau d'octet
			//InputStream transforme ça en un octet
			try {
				String adresseIP = socket.getLocalSocketAddress().toString();
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader buffer = new BufferedReader(isr);
				
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				
				pw.println("Bienvenue vous etes le client " + clientNumber);
				pw.println("Devinez un nombre : ");
				
				System.out.println("Le client avec l'adresse " + clientNumber + " se connecte");
				while(true) {
					
					String requete = buffer.readLine();
					int nombre = Integer.parseInt(requete);
					
					if(fin == false) {
						
						if(nombre < secretNumber) {
							pw.println("Le nombre que vous avez entre est un nombre inferieur au nombre secret");
						
						}
						if(nombre > secretNumber) {
							pw.print("Le nombre que vous avez entre est un nombre superieur au nombre secret");
						}else {
							pw.println("Bravo, vous avez gagne");
							pw.println("Le nombre secret etait " + secretNumber);
							pw.println("Le jeu est termine, le client " + numeroClient + " a gagne");
							System.out.println("Le client avec l'adresse " + adresseIP + " a remporte  la partie");
							fin=true;
						}
					}else {
						pw.println("GAME OVER");
					}
				}
			
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}

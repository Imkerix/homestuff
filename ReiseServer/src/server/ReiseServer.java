package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.crypto.AEADBadTagException;

import client.ReiseClient;

/**
 * Provides the server functionality it allows clients to connect and ask for
 * bookings, free places and provided traveling destinations.
 * 
 * @author erik
 * 
 */
public class ReiseServer {
	private ServerSocket serversocket = null;
	private ReiseContainer reiseContainer = new ReiseContainer();
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * Creates a new {@link ServerSocket} and makes multiple client access
	 * possible with threads. In the contained overwritten run method the
	 * {@link Constructor} will check what the {@link ReiseClient} wants to do.
	 * Multiple requests are possible. The run method simply does one thing
	 * after the other, untill the client sends "clientIsGoingDown" what causes
	 * the end of the connection.
	 * 
	 * @param port an Integer that stands for the Port the Server will be available at (don&#180;t forget to configure your firewall).                      
	 * @throws IOException
	 */
	public ReiseServer(int port) throws IOException {
		serversocket = new ServerSocket(port);
		
		new Thread(){
			@Override
			public void run(){
				while(true){
					try(Socket clientserver = serversocket.accept()){
						System.out.println("Client "+clientserver.getInetAddress()+" connected! "+clientserver.getPort());
						in = new BufferedReader(new InputStreamReader(clientserver.getInputStream()));
						out = new PrintWriter(clientserver.getOutputStream(), true);

						boolean clientActive = true;
						while (clientActive) {
							String whatshellido = in.readLine();							

							if(whatshellido.equals("buchen")){

								String reiseziel = in.readLine();
								int mitfahrer = Integer.parseInt(in.readLine());										
								bucheReise(clientserver, reiseziel, mitfahrer);

							}
							if(whatshellido.equals("getReiseziele")){

								out.println(reiseContainer.getReiseziele().length); //laenge des String[] in Reiseclient
								for(String s : reiseContainer.getReiseziele()){
									out.println(s);		
								}	
							}
							if(whatshellido.equals("getFreiePlaetze")){
								String reiseziel = in.readLine();		
								out.print(getFreiePlaetze(reiseziel));
							}
							if(whatshellido.equals("clientIsGoingDown")){
								clientActive = false;
								if(out != null)
									out.close();
								if(in != null)
									in.close();
								clientserver.close();	
								System.out.println("Client "+clientserver.getInetAddress()+" disconnected!");
							}

						}
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}.start();

	Runtime.getRuntime().addShutdownHook(new Thread() { // Loest speicher befehl beim Beenden der VM aus

		public void run() {
			try {
				
			reiseContainer.speichern();
					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	});


	}

	/**
	 * Gets free places in a {@link Reise} object. <br>
	 * (Is only out sourced code from {@link ReiseServer.ReiseServer(int port}).
	 * 
	 * @param reiseziel a String that is used to find a inherent {@link Reise}.          
	 * @return an Integer that represents the amount of avilable places in the {@link Reise} object that matches to {@link reiseziel}.       
	 */
	public  int getFreiePlaetze(String reiseziel) {

		int freiePlaetze = 0;

		
			for (String s : reiseContainer.getReiseziele()) {
				if (s.equalsIgnoreCase(reiseziel)) {
					//	System.out.println(reiseContainer.getReise(reiseziel).freiePlaetze());
					freiePlaetze = reiseContainer.getReise(reiseziel).freiePlaetze();
				} 
			}
			
		return freiePlaetze;
	}

	/**
	 * Contains the server part of the booking protocol written for the
	 * communication with the {@link ReiseClient} <br>
	 * <br>
	 * 1: The method checks if it provides a journey matching to the {@link reiseziel}.<br>
	 * 2: The method checks the matching journey has enough space for the amount of persons who want to book this journey.<br>
	 * 3: The method answerers the client over an outputstream if the booking request is OK (journey exists and enough space).<br>
	 * 4: If the request was ok the Client now sends the names of the individual travelers.<br>
	 * 5: The Server tries to add every traveler to the desired {@link Reise} if everything works the {@link ReiseServer} will answer posetive.<br>
	 * 
	 * @param client a Socket that is used to communicate with the client.
	 * @param reiseziel a String that represents the travel destination the booking is about.
	 * @param anzahl an Integer that represents the number of future travelers who want to book.
	 * @throws IOException 
	 */
	public synchronized void bucheReise(Socket client, String reiseziel, int anzahl) throws IOException {

		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		
		
		//Anfang der Antwort an den Client	
		boolean isOK = false;						
		for(String s : reiseContainer.getReiseziele()){
			if(s.equalsIgnoreCase(reiseziel)){
				isOK = true;
			}
		}						

		if(isOK){ //ist das Reiseziel vorhanden?									
			if(reiseContainer.getReise(reiseziel).freiePlaetze()>=anzahl){ //ist in der Reise noch platz?
				out.println("+OK Namen der Teilnehmer Eingeben...");
				//isOK = true;
			}
			else{
				out.println("-ERR Reservierung nicht möglich"); //Kein Platz								
				isOK = false;
			}	
		}
		else{
			out.println("-ERR Unbekanntes Reiseziel"); //Das Reise ziel ist nicht vorhanden
			//isOK = false;
		}

		// Ende der Antwort an den Client	
		// Warten auf Teilnehmer Namen und sofortiges weiterreichen an die Reise				
		if(isOK){
			
			ArrayList<String> all = new ArrayList<String>();
			String next = "";
			for(int i = 0; i<anzahl; i++){
				next = in.readLine();
				all.add(next);
			}
			for(String s : all){
				reiseContainer.getReise(reiseziel).hinzufuegenTeilnehmer(s);
			}
			
			
//			for(String s = in.readLine(); s != null; reiseContainer.getReise(reiseziel).hinzufuegenTeilnehmer(s)){	
//			}
								 	
			// Namen der Teilnehmer sind da	und wurden weitergereicht		

			//Bestaetigung schiken
			out.println("+OK Reservierung erfolgt");
			//Ende Bestaetigung	
		}
		else{
			out.println("Ihre Buchung ist fehlgeschalgen!");
		}
	}

}
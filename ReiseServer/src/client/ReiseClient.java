package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import server.Reise;
import server.ReiseServer;

/**
 * {@link ReiseClient} Represents the Client who want to book something at the {@link ReiseServer}.
 * @author erik heinisch
 */
public class ReiseClient {
	
	private Socket socket; /** Endpoint on the client machine*/
	private PrintWriter out; /** Sends Strings and objects through the socket to the server*/
	private BufferedReader in; /** Reads answerers from the {@link ReiseServer} */
	

	/**
	 * Sends a String to the connected {@link ReiseServer} which commands to close the connected socket.
	 * @return a boolean that represents the success of the method: if true = method successful if false = method failed.
	 */
	public boolean abmelden() {
		try {	
			out.println("clientIsGoingDown");
			socket.close();
			return true;			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * The client part of the booking protocol written for the communication with the {@link ReiseServer}.<br>
	 * <br>
	 * 1: client writes that it want to book something.<br>
	 * 2: client specifies what it wants to book an tells the travel destination "reiseziel" and the amount of needed places {@link teilnehmer}.<br>
	 * 3: If the Server responds are ok the method goes on and sends the names of the people who wnat to travel.<br>
	 * 4: the method returns true if the {@link ReiseServer} responds positve.<br>
	 * 
	 * @param reiseziel a String that is used to lookup the {@link Reise} object
	 * @param teilnehmer a Sting[] that contains the names of the people who want to book the journey specified by {@link reiseziel}
	 * @return a boolean that represents the success of the method: if true = method successful if false = method failed
	 * @throws IOException
	 */
	public  boolean buchen(String reiseziel, String[] teilnehmer) throws IOException {
		
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			boolean isOK = false;
		//Gebe buchungs Wunsch an
				out.println("buchen");
		//Ende Buchungswunsch
		// Gebe Reiseziel und anzahl der Gewuenschten personen an server
				out.println(reiseziel);
				out.println(teilnehmer.length);
		// Ende uebergabe	
		//Bekomme Antwort
				String answer1 = in.readLine();
				if(answer1.startsWith("-")){ //Wenn true war anfrage nicht erfuelbar
					System.err.println("Fehler im Protokoll (Reise Client)");
					abmelden();
					
				}
				if(answer1.startsWith("+")){ //Wenn true war anfrage erfolgreich
			// Sende Namen
						isOK = true;
						for(String s : teilnehmer){
							out.println(s);
							
						}
		    //Ende Sende Namen		
				}else{
					System.err.println("Fehler im Protokoll (Reise Client)");
				}
				
		//Ende Bekomme Antwort
		//Erwarten der Bestätigung des Servers	
				if(isOK){
						String answer2 = in.readLine();
							
					if(answer2.startsWith("+")){
						System.out.println("Erfolg Du hast gebucht "+ reiseziel);
						
						return true;
					}	
					else{
	
						return false;
					}
				}
				else{
					return false;
				}
		//Fertig!!	
				
	}
	
	/** 
	 * Returns the exact amount of places that are still free for the by {@link reiseziel} identified journey.
	 * @param reiseziel With the travel destination String it is possible to search for the searched journey.
	 * @return An Integer that tells the number of free places in the {@link Reise} Object. 
	 * @throws IOException
	 */
	public int getFreiePlaetze(String reiseziel) throws IOException {
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		out.println("getFreiePlaetze");
		out.println(reiseziel);
		
		return Integer.parseInt(in.readLine());
	}

	/**
	 * Returns all travel destinations known by the {@link ReiseServer}.
	 * String by String it gets the travel destinations and converts them into a String[]
	 * @return a String[] that includes all travel destinations known by the {@link ReiseServer}
	 * @throws IOException 
	 */
	public  String[] getReiseziele() throws IOException {
		
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		out.println("getReiseziele");
	
		
		
		int length = Integer.parseInt(in.readLine());
		//String nextLine = in.readLine();
	     
	    String[] alleReisen = new String[length];
	    for(int i = 0; i<alleReisen.length; i++){
	    	alleReisen[i]=in.readLine();
	    }
	    
		return alleReisen;
	}

	/**
	 * Called to establish a connection with a Server  
	 * @param server a string that represents the web address of the server {@link verbinden} tries to connect with. 
	 * @param port a integer that represents the port on the server {@link verbinden} tries to connect with.
	 * @return a boolean that informs about the success of the connection attempt
	 * @see ReiseServer
	 */
	public  boolean verbinden(String server, int port) {
		try {
			socket = new Socket(server, port);	
			return true;
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		} 
	}

}

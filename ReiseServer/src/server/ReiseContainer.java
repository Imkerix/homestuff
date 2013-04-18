package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents some kind of administration box inside the {@link ReiseServer} it
 * could be very useful to have more of these things and split the {@link Reise}
 * objects into category's, but that is not implemented by default.
 * 
 * @author erik heinisch
 * 
 */
public class ReiseContainer {
	// Feel free to change this path to suit your needs
	private String dateiort = "reisen.txt"; /** The relative path to the save file of the {@link ReiseContainer}.*/ 
	private ArrayList<Reise> dieReisen;	/** Used to manage all {@link Reise} objects, available.*/

	/**
	 * Creates two ArrayList: one for {@link Reise} objects and one for the travel destinations.<br>
	 * As last action the {@link Constructor} calls the {@link this.laden()}
	 * method to make sure that {@link Reise} Objects that existed in the last
	 * session are restored.
	 * 
	 * @throws IOException
	 */
	public ReiseContainer() throws IOException {	
		dieReisen = new ArrayList<Reise>();
		this.laden();
	}

	/**
	 * Add&#180;s a {@link Reise} and the inherent journey destination to the specific ArrayList&#180;s
	 * 
	 * @param ziel a String that represents the travel destination of the inherent {@link Reise}.
	 * @param reise a {@link Reise} object that is inherent with {@link ziel}.           
	 */
	public void addReise(String ziel, Reise reise) {
		if(reise != null){		
			dieReisen.add(reise);		
		}
	}

	/**
	 * Get&#180;s a {@link Reise} object that fits to the given travel destination
	 * 
	 * @param ziel a String that is used to find a matching {@link Reise} object
	 * in {@link dieReisen}, ziel is the destination of the journey to find
	 * @return a {@link Reise} object that was selected as matching for the  parameter {@link ziel}.        
	 */
	public Reise getReise(String ziel) {
			for (Reise s : dieReisen) {
			if (s.getZiel().equalsIgnoreCase(ziel)) {
				return s;
			}
		}
			return null;
	}

	/**
	 * Get&#180;s all travel destinations
	 * @return a String[] that contains all travel destinations provided by the {@link ReiseContainer}        
	 */
	public String[] getReiseziele() {
		
		String[] reisearray = new String[dieReisen.size()];
		for (int i = 0; i < dieReisen.size(); i++) {
			reisearray[i] = dieReisen.get(i).getZiel();
		}
		return reisearray;
	
	}

	/**
	 * Reads the file "reisen.txt". For each line of the
	 * file it creates one {@link Reise} add&#180;s the travelers to them and finaly add&#180;s the
	 * {@link Reise} into {@link dieReisen}. It is called
	 * automatically in the creation of an new {@link ReiseContainer}
	 * 
	 * @throws IOException
	 */
	public void laden() throws IOException {
		
		Scanner sc =  new Scanner(new File(dateiort));
		sc.useDelimiter(System.getProperty("line.separator")); 
		while(sc.hasNext()){
			String line = sc.next();
			if(line.equals("")){	
			}
			else{	
				String[] test = line.split(";"); 							//Broeselt Zeile in die Strings die zu Objekten werden sollen
				Reise rei = new Reise(test[0],Integer.parseInt(test[1])); 	//Neue Reise mit den immer in [0] und [1] liegenden Grundwerten
				for(int i = 2; i<test.length; i++){							//Teilnehmer eintragen falls sie da sind
					rei.hinzufuegenTeilnehmer(test[i]);
				}
				addReise(test[0], rei);		
			}
		}
		sc.close();		
	}

	/**
	 * Get&#180;s the amount of journeys available in the {@link ReiseContainer}
	 * @return an Integer that represents the number of journeys administrated by the server.       
	 */
	public int size() {
		return dieReisen.size();
	}

	/**
	 * Writes a save file "reisen.txt" in which all journeys and
	 * their values are stored. speichern() is called automatically when the JVM
	 * shuts down, so the Server saves on its own.
	 * 
	 * @throws IOException
	 */
	public void speichern() throws IOException {

		FileWriter ausgabeStream = new FileWriter(dateiort, false);
		
		for (int i = 0; i < dieReisen.size(); i++) {
			ausgabeStream.write(dieReisen.get(i).getZiel() + 
			";" + dieReisen.get(i).anzahlPlaetze() + 
			dieReisen.get(i).toString() + System.getProperty("line.separator"));
		}
		ausgabeStream.close();

	}

	/**
	 * Nothing important in here but it can be used for debugging
	 * 
	 * @return dateiname a String that represents nothing at all because I
	 *         don&#180;t need this method in my build and i did just use it for
	 *         debugging, and to get a nice overview on what is in
	 *         {@link dieReisen}
	 */
	public String toString() {
		for (Reise r : dieReisen) {
			System.out.println(r.getZiel());
			System.out.println("Gesamt anzahl der Plätze " + r.anzahlPlaetze());
			System.out.println("Freie Plätze " + r.freiePlaetze());
			System.out.println("//Mitfahrer//");

			for (String s : r.getTeilnehmerListe()) {
				System.out.println(s);
			}
			System.out.println();
		}
		return dateiort;
	}

}

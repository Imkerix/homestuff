package server;

/**
 * Represents a journey used by the {@link ReiseServer}.
 * @author erik heinisch
 */
public class Reise {


	private int anzahl; 	/** The amount of people registrated by this {@link Reise} object */
	private int max;    	/** The maximal number of people allowed on this journey */
	private String ziel;	/** The travel destination */
	private Teilnehmer ersterTeilnehmer;	/** The beginning of the easy list which is used to manage the travelers */

	/**
	 * Synchronizes the values of its parameters with the attributes in the class.
	 * @param ziel a String representing the place where the journey is going to.
	 * @param max an Integer representing the maximal number of people traveling in this journey.
	 * @param teilnehmer a String[] that includes the names of all travelers.
	 */
	public Reise(String ziel, int max, String[] teilnehmer) {
		this.setZiel(ziel);
		this.max = max;
		this.anzahl = 0;

		if(teilnehmer != null){
			for(String s : teilnehmer){
				hinzufuegenTeilnehmer(s);
			}
		}
	}

	/**
	 * Calls the other constructor and add the parameter null for the String[] to match the conditions 
	 * of Reise(String ziel, int max, String[] teilnehmer).
	 * @param ziel a String representing the place where the journey is going to.
	 * @param max an Integer representing the maximal number of people traveling in this journey.
	 */
	public Reise(String ziel, int max) {
		this(ziel,max,null);		
	}

	/**
	 * Finds out how many people can travel in this journey maximal.
	 * @return an Integer "max" that tells you about the maximum number of people who can travel in this journey.
	 */
	public int anzahlPlaetze() {
		return max;
	}

	/**
	 * Finds out how much traveling places are still free in a journey.
	 * @return an Integer that represents the amount of free places in the journey. 
	 */
	public int freiePlaetze() {
		return max - anzahl;
	}

	/**
	 * Return&#180;s the names of all travelers connected to the journey.
	 * @return a String[] that includes the names of all travelers connected to this journey.
	 */
	public String[] getTeilnehmerListe() {

		String[] teilnehmer = new String[anzahl];

		Teilnehmer traveler = ersterTeilnehmer;
		for(int i = 0; traveler!=null; i++){		
			teilnehmer[i] = traveler.name;		
			traveler = traveler.naechster;
		}		
		return teilnehmer;
	}

	/**
	 * Add&#180;s a traveler to the {@link Reise} to do so the traveler gets sorted in the established list by name ascending.
	 * @param name a String that represents the name of the person to add in the easylist of the {@link Reise} object.
	 * @return a boolean that represents the success of the method: if true = method successful if false = method failed.
	 */
	public boolean hinzufuegenTeilnehmer(String name) {
		if(max - anzahl > 0){
			anzahl++;
			Teilnehmer temp = new Teilnehmer(name, null);

			Teilnehmer e = ersterTeilnehmer;
				
			if(e == null){
				ersterTeilnehmer = temp;
			}
			else if (e.name.compareToIgnoreCase(name) >= 0){
				temp.naechster = e;
				ersterTeilnehmer = temp;
			}
			else {
				while (e.naechster != null && e.naechster.name.compareToIgnoreCase(name) <= 0){
					e = e.naechster;
				}	
				temp.naechster = e.naechster;
				e.naechster = temp;
			}
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Returns a String with all persons on the {@link Reise}. The String is started by ";" and all names
	 * are also seperated by an ";" its implemented this way because the method is used to complete the String which is written in the reisen.txt file
	 * by the {@link ReiseContainer.speichern()} method to represent this {@link Reise} object.  
	 * @see {@link java.lang.String.toString()} Similar kind of method.
	 * @return alleTeilnehmer a String that includes all {@link Teilnehmer} out of the easylist ( first element = {@link ersterTeilnehmer}.
	 */
	public String toString() {
		String alleTeilnehmer = "";

		Teilnehmer s = ersterTeilnehmer;
		while(s != null){
			alleTeilnehmer+=";"+s.name;
			s = s.naechster;
		}		
		return alleTeilnehmer;
	}

	/**
	 * Getter method for the private String ziel.
	 * @return the direction of the journey.
	 */
	public String getZiel() {
		return ziel;
	}

	/**
	 * Setter method for the private String ziel.
	 * @param "ziel" the direction to set for the journey.
	 */
	public void setZiel(String ziel) {
		this.ziel = ziel;
	}

}
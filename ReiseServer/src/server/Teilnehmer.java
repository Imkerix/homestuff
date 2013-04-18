package server;

/**
 * 
 * Represents the travelers in the {@link ReiseServer}
 * @author erik
 */
public class Teilnehmer {
	
	public Teilnehmer naechster; /** Points at another {@link Teilnehmer} object or at null*/
	public String name; /** The name of the traveler represented by {@link Teilnehmer}*/

	/**
	 * Writes the given parameters values to the actual attributes.
	 * The parameters are made for the use in an easy list
	 * @param name the name of the person represented by {@link Teilnehmer} 
	 * @param naechster the next {@link Teilnehmer} Object in the Easy List
	 */
	public Teilnehmer(String name, Teilnehmer naechster) {
		
		this.naechster = naechster;
		this.name = name;
	}
	
}
package dbStrings;

/**Klasse mit statischen Strings die so von der Datenbank verwendet
 * werden k�nnen.
 * 
 * <p> Die einzelnen Datenbankabfragen (Query) werden mit verweisen auf
 * diese statischen Strings zusammengesetzt. So wird sichergestellt, dass
 * eine Abfrage der Daten auch sicher gefunden wird.
 * 
 * <p> W�rden die Queries direkt als String geschrieben, so best�nde 
 * die Gefahr, dass durch ein Schreibfehler (z.B. Gross- / Kleinschreibung)
 * eine Abfrage abgebrochen wird weil der gesuche Wert von der Datenbank 
 * nicht gefunden werden kann.
 * 
 * @author Ron Peyer
 * @version 1.0
 * @since March 2018
 * 
 */
public class StringsForDB {
	
	/** Strings f�r die Datenbank und deren Tabellen. */
	public static String keba= "keba";
	public static String tableLoadings= "tbloadings";
	
	public static String userid = "userid";
	public static String userName = "username";
	public static String passwd = "passwd";
	
	public static String loadID= "loadid";
	public static String loadStartDate= "startdate";
	public static String loadEnddate= "enddate";
	public static String loadTime= "loadtime";
	public static String plugTime= "plugtime";
	public static String loadedEnergy= "loadedEnergy";
	public static String loadJson = "loadjson";
	public static String loadFilePath= "loadfilePath";
	public static String statefilePath= "statefilePath";
	public static String stateJson = "statejson";
	public static String loadingComplete = "loadcomplete";
	public static String sessionID= "sessionid";
	public static String loadingUsable= "usable";
	
}

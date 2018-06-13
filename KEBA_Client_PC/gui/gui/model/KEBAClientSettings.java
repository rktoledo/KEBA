package gui.model;

public class KEBAClientSettings {
	
private static KEBAClientSettings instance = null;
	
	/**
	 * Singleton-Konstruktor des eine einmalige Instanz zur�ckliefert
	 * @return
	 */
	public static KEBAClientSettings getInstance() {
		if (instance == null) {
			instance = new KEBAClientSettings();
		}
		return instance;
	}
	
	public String HOSTNAME = "RSK";
	
	public int REFRESH_RATE= 10000; 	// GUI refresh rate in milliseconds
}

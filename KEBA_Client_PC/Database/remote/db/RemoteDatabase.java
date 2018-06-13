package remote.db;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import keba.rmiinterface.KEBADBInterface;

public class RemoteDatabase {
	
	private static RemoteDatabase instance = null;
	private KEBADBInterface db;
	
	/**
	 * Singleton-Konstruktor des eine einmalige Instanz zurueckliefert
	 * @return
	 */
	public static RemoteDatabase getInstance() {
		if (instance == null) {
			instance = new RemoteDatabase();
		}
		return instance;
	}
	
	private RemoteDatabase(){
		this.db= setRemoteObjects();
	}
	
	public KEBADBInterface getDB(){
		return this.db;
	}
	
	private KEBADBInterface setRemoteObjects(){
		String host = "RSK";
        
    	KEBADBInterface dbStub= null;
        
		try {        	
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            dbStub = (KEBADBInterface) registry.lookup(KEBADBInterface.SERVICE_NAME);
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            System.out.println("Server not started");
            e.printStackTrace();
        }
		return dbStub;
	}
}

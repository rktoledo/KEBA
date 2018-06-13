package dbDAOInterface;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import dbObjects.LoadingData;

import serverobjects.LatestSessionID;

/**
 * Interface f�r die Erzeugung der Datenbank "FLADAN" und deren Tabellen
 * 
 * @author Ron Kirchhofer
 * @version 1.0
 * @since Jul.2014
 *
 */
public interface DbDAO{
    
    public boolean createDB();
    
    public boolean createTable(String command, String tablename);
    
    public void setSessionID(int loadID, int sessionID);
    
    public int getSessionID(int loadID);
    
    public int createLoading(LocalDateTime ldtStart);
    
    public int createLoading(LocalDateTime ldtStart, int sessionID);
    
    public LocalDateTime getLoadingStart(int loadID);
    
    public boolean addLoadingEnd(LocalDateTime ldtEnd);
    
    public LocalDateTime getLoadingEnd(int loadID);
    
    public int getLoadTime(int loadID);
    
    public int addLoadTime(int loadID, LocalDateTime loadingEndTime);
    
    public int getLoadedEnergy(int loadID);
    
    public int addLoadedEnergy(int loadedEnergy);
    
    public void addLoadingFilePath(String loadingFilePath);
    
    public String getLoadingFilePath(int loadID);
    
    public String getLoadingFilePathbySessionID(int sessionID);
    
    public void addStateFilePath(String stateFilePath);
    
    public String getStateFilePath(int loadID);
    
    public String getStateFilePathbySessionID(int sessionID);
    
    public boolean terminateLoading(LocalDateTime loadingEndTime, String loadFilePath, String stateFilePath, int loadedEnergy);
    
    public int calculateDifference(LocalDateTime startDateTime, LocalDateTime endDateTime);
 
    public int calculateLoadTime(int loadID, LocalDateTime endDateTime);
    
    public boolean checkIfSessionIDExists(int sessionID);

	public void setLatestSessionID(LatestSessionID latestSessionID);

	public void setCurrentlLoadingID(int loadingID);

	public int getCurrentLoadingID();

	public int getCurrentLoadingID(int sessionID);
	
	//public int getLatestSessionID();
	
	public ArrayList<LoadingData> getLoadingsFrom(int loadID) throws RemoteException;
	
	public ArrayList<LoadingData> getLoadingsTo(int loadID) throws RemoteException;
	
	public ArrayList<LoadingData> getLoadingsFromTo(int loadIDstart, int loadIDend) throws RemoteException;
	
	public ArrayList<LoadingData> getAllLoadings() throws RemoteException;
	
	public LoadingData getLoading(int loadID) throws RemoteException;
    
}

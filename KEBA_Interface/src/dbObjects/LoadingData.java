package dbObjects;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;

public class LoadingData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int loadID;
	private int sessionID;
	private int loadTime;
	private int loadedEnergy;
	private boolean isComplete;
	private boolean isUsable;
	private boolean isSessionIDSet;
	
	private boolean isLoadFileAvailable;
	private String loadFilePath;
	private JSONObject loadFile;
	
	private boolean isStateFileAvailable;
	private String stateFilePath;
	private JSONObject stateFile;
	
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	
	public LoadingData(){
		
	}
	
	public LoadingData(int loadID, int sessionID, LocalDateTime startDateTime, LocalDateTime endDateTime, 
				int loadTime, JSONObject loadFile, JSONObject stateFile, boolean isComplete, boolean isUsable, int loadedEnergy, 
				Boolean isSessionIDSet){
		this.loadID= loadID;
		this.sessionID= sessionID;
		this.loadTime= loadTime;
		this.isComplete= isComplete;
		this.isUsable= isUsable;
		this.loadedEnergy= loadedEnergy;
		this.startDateTime= startDateTime;
		this.endDateTime= endDateTime;
		this.loadFile= loadFile;
		this.stateFile= stateFile;
		this.setSessionIDSet(isSessionIDSet);
	}

	public int getLoadID() {
		return loadID;
	}

	public int getSessionID() {
		return sessionID;
	}

	public int getLoadTime() {
		return loadTime;
	}

	public int getLoadedEnergy() {
		return loadedEnergy;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public boolean isUsable() {
		return isUsable;
	}

	public boolean isLoadFileAvailable() {
		return isLoadFileAvailable;
	}

	public JSONObject getLoadFile() {
		return loadFile;
	}

	public boolean isStateFileAvailable() {
		return isStateFileAvailable;
	}

	public JSONObject getStateFile() {
		return stateFile;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	
	/*public String getStartDateTimeString() {
		return startDateTime;
	}*/

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setLoadID(int loadID) {
		this.loadID = loadID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}

	public void setLoadedEnergy(int loadedEnergy) {
		this.loadedEnergy = loadedEnergy;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}

	public void setLoadFileAvailable(boolean isLoadFileAvailable) {
		this.isLoadFileAvailable = isLoadFileAvailable;
	}

	public String getLoadFilePath() {
		return loadFilePath;
	}

	public void setLoadFilePath(String loadFilePath) {
		this.loadFilePath = loadFilePath;
	}

	public void setLoadFile(JSONObject loadFile) {
		this.loadFile = loadFile;
	}

	public void setStateFileAvailable(boolean isStateFileAvailable) {
		this.isStateFileAvailable = isStateFileAvailable;
	}

	public String getStateFilePath() {
		return stateFilePath;
	}

	public void setStateFilePath(String stateFilePath) {
		this.stateFilePath = stateFilePath;
	}

	public void setStateFile(JSONObject stateFile) {
		this.stateFile = stateFile;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = null;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = null;
	}
	
	public boolean isSessionIDSet() {
		return isSessionIDSet;
	}

	public void setSessionIDSet(boolean isSessionIDSet) {
		this.isSessionIDSet = isSessionIDSet;
	}

	public String toString(int seconds){
		int days= seconds/(24*60*60);
		seconds= seconds-days*(24*60*60);
		int hours= seconds/(60*60);
		seconds= seconds-hours*(60*60);
		int min= seconds/60;
		seconds= seconds-min*60;
		String str= days + " days, " + hours + " hours " + min + " minutes " + seconds + " seconds";
		return str;
	}
	
}

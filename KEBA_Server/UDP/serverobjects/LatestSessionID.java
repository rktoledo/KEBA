package serverobjects;

public class LatestSessionID {
	private int latestSessionID;
	
	public LatestSessionID() {
	}
	
	public void setLatestSessionID(int sessionID){
		this.latestSessionID= sessionID;
	}
	
	public int getLatestSessionID(){
		return this.latestSessionID;
	}
}

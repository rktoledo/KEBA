package gui.model;

import java.io.Serializable;
import java.util.Observable;

public class ServerStateObservable extends Observable implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean isRunning= false;
	
	public ServerStateObservable(boolean isRunning){
		this.isRunning= isRunning;
	}

	public boolean getisRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		setChanged();
	    notifyObservers();
	}
	
	
}

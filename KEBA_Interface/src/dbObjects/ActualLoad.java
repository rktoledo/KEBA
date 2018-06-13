package dbObjects;

import java.io.Serializable;
import java.util.Observable;

public class ActualLoad extends Observable implements Serializable{
	private static final long serialVersionUID = 7815040133943067596L;
	
	private Report100 loadingData;
	private LoadingObject loadingDetails;
	private Boolean isComplete= false;
	private Boolean isNewLoad= false;
	
	
	public ActualLoad(){
	}
	
	public ActualLoad(Report100 loadingData, LoadingObject loadingDetails){
		this.setLoadingData(loadingData);
		this.setLoadingDetails(loadingDetails);
	}
	
	public ActualLoad(Report100 loadingData, LoadingObject loadingDetails, Boolean isComplete){
		this.setLoadingData(loadingData);
		this.setLoadingDetails(loadingDetails);
		this.isComplete= isComplete;
	}

	public LoadingObject getLoadingDetails() {
		return loadingDetails;
	}

	public void setLoadingDetails(LoadingObject loadingDetails) {
		this.loadingDetails = loadingDetails;
		setChanged();
		notifyObservers(this);
	}

	public Report100 getLoadingData() {
		return loadingData;
	}

	public void setLoadingData(Report100 loadingData) {
		this.loadingData = loadingData;
		setChanged();
		notifyObservers(this);
	}

	public Boolean isComplete() {
		return isComplete;
	}

	public void setComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public Boolean isNewLoad() {
		return isNewLoad;
	}

	public void setNewLoad(Boolean isNewLoad) {
		this.isNewLoad = isNewLoad;
	}
}

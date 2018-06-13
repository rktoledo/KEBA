package gui.controller;

import dbObjects.ActualLoad;
import dbObjects.LoadingData;
import gui.view.LoadingInfoView;

public class LoadingInfoController {
	private LoadingInfoView view;
	private LoadingData loading;
	
	public LoadingInfoController(LoadingData loading, ActualLoad actualLoad, Boolean isLarge){
		this.loading = loading;
		this.loading.setLoadTime(actualLoad.getLoadingDetails().getChargingime());
		this.view = new LoadingInfoView(this.loading, actualLoad, isLarge);

		updateInfo(actualLoad);
		//System.out.println("Initial value Epres= " + loading.getLoadedEnergy());
	}
	
	public LoadingInfoView getView(){
		return this.view;
	}
	
	public void updateInfo(ActualLoad load){
		if (load != null){
			//System.out.println("LIC uüdate Epres: " + load.getLoadingData().getEpres());			
			this.view.updateView(load);
		}
	}
}

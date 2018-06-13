package gui.controller;

import gui.model.Informations;
import gui.view.InfoView;

public class InfoController {
	private InfoView infoView;
	private Informations info;

	public InfoController(){
		this.info= new Informations();
		this.infoView= new InfoView();
	}
	
	public InfoView getInfoView() {
		return this.infoView;
	}
}

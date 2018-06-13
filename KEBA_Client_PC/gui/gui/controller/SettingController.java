package gui.controller;

import gui.model.KEBAClientSettings;
import gui.view.SettingsView;

public class SettingController {

	private SettingsView settingsView;
	private KEBAClientSettings settings;

	public SettingController(){
		this.settings= new KEBAClientSettings();
		this.settingsView= new SettingsView();
	}
	
	public SettingsView getSettingView() {
		return this.settingsView;
	}
}

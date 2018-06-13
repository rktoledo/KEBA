package gui.controller;

import gui.view.LoadingTableView;
import keba.rmiinterface.KEBADBInterface;

public class LoadingTableController {
	
	private KEBADBInterface db;
	private LoadingTableView loadingView;

	public LoadingTableController(KEBADBInterface db){
		this.db= db;
		this.loadingView= new LoadingTableView();
	}
	
	public LoadingTableView getLoadingView() {
		return this.loadingView;
	}
}

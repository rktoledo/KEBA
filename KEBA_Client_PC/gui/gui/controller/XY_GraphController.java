package gui.controller;

import javax.swing.JPanel;

import dbObjects.LoadingObject;
import gui.view.XY_Graph;

public class XY_GraphController {
	
	private JPanel view;
	private LoadingObject loading;
	private String type;
	
	public XY_GraphController(LoadingObject loading, String type, String unit, Boolean isLarge){
		this.loading= loading;
		this.type= type;
		this.view= new XY_Graph(this.loading, this.type, unit, isLarge);
	}
	
	public JPanel getView(){
		return view;
	}
	
	public void updateInfo(LoadingObject loading){
		this.loading= loading;
		((XY_Graph) this.view).updateGraph(this.loading);
	}
	
	public JPanel getChart(){
		return ((XY_Graph)this.view).getChart();
	}
	
	public void newDataset(LoadingObject loading){
		this.loading= loading;
		((XY_Graph) this.view).newGraph(this.loading);
	}
}

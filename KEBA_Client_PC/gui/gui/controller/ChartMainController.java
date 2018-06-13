package gui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import dbObjects.ActualLoad;
import dbObjects.LoadingData;
import dbObjects.LoadingObject;
import dbObjects.Report100;
import gui.model.KEBAClientSettings;
import gui.view.ChartView;
import gui.view.LoadingInfoView;
import gui.view.XY_Graph;
import keba.rmiinterface.KEBADBInterface;
import keba.rmiinterface.KEBAFileInterface;
import keba.rmiinterface.KEBAInterface;

public class ChartMainController {
	
	private KEBADBInterface db;
	private KEBAFileInterface fileHandler;
	private KEBAInterface udpServer;
	
	private ChartView chartView;
	
	private XY_GraphController sumEnergyGraphController;
	private XY_GraphController sumVoltageGraphController;
	private XY_GraphController sumCurrentGraphController;
	private LoadingInfoController sumLoadingInfoController;
	
	private JPanel sumEnergyGraph;
	private JPanel sumVoltageGraph;
	private JPanel sumCurrentGraph;
	private LoadingInfoView sumInfoView;
	private LoadingData loadingData= null;
	
	private XY_GraphController energyGraphController;
	private XY_GraphController voltageGraphController;
	private XY_GraphController currentGraphController;
	private LoadingInfoController loadingInfoController;

	private JPanel energyGraph;
	private JPanel voltageGraph;
	private JPanel currentGraph;
	private LoadingInfoView infoView;
	
	private ActualLoad actualLoad;
	
	private KEBAClientSettings settings;
	

	public ChartMainController(KEBADBInterface db, KEBAFileInterface fileHandler, KEBAInterface udpServer){
		this.db= db;
		this.fileHandler= fileHandler;
		this.udpServer= udpServer;
		
		settings= KEBAClientSettings.getInstance();
		
		this.actualLoad= getData();
		
		createGraphs(actualLoad);
		
		this.sumLoadingInfoController = new LoadingInfoController(this.loadingData, actualLoad, false);
		this.sumInfoView= sumLoadingInfoController.getView();
		this.loadingInfoController = new LoadingInfoController(this.loadingData, actualLoad, true);
		this.infoView= loadingInfoController.getView();
		
		this.chartView= new ChartView(sumEnergyGraph, sumVoltageGraph, sumCurrentGraph, sumInfoView,
				energyGraph, voltageGraph, currentGraph, infoView);
		
		addMouseListener(new BoxListener());
	}
	
	public ActualLoad getData(){
		String actualLoadFilePath= null;
		LoadingObject loading= null;
		Report100 rep100= null;
		ActualLoad actualLoad= null;
		try {
			this.loadingData= this.db.getActualLoading();
			actualLoadFilePath = loadingData.getLoadFilePath();
			loading= this.fileHandler.getLoadingData(actualLoadFilePath);
			loading.setSessionID(loadingData.getSessionID());
		
			//if (!loadingData.isComplete()){
				rep100= this.udpServer.report100();
				actualLoad= new ActualLoad(rep100, loading);
			//}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return actualLoad;
	}
	
	/*public void updateGUI(){
		ActualLoad actualLoad= getData();
		updateInfo(actualLoad);
	}*/
	
	public void addMouseListener(BoxListener listener){
		((XY_Graph)this.sumEnergyGraph).getChart().addMouseListener(listener);
		((XY_Graph)this.sumVoltageGraph).getChart().addMouseListener(listener);
		((XY_Graph)this.sumCurrentGraph).getChart().addMouseListener(listener);
		this.sumInfoView.addMouseListener(listener);
		
		((XY_Graph)this.energyGraph).getChart().addMouseListener(listener);
		((XY_Graph)this.voltageGraph).getChart().addMouseListener(listener);
		((XY_Graph)this.currentGraph).getChart().addMouseListener(listener);
		this.infoView.addMouseListener(listener);
	}
	
	public void setConnectionState(Boolean isConnected){
		
	}
	

	public ChartView getChartView() {
		return this.chartView;
	}
	
	public void createGraphs(ActualLoad actualLoad){
		this.sumEnergyGraphController= new XY_GraphController(actualLoad.getLoadingDetails(), "Energy", "Energy [kWh]", false);
		this.sumVoltageGraphController= new XY_GraphController(actualLoad.getLoadingDetails(), "Voltage", "Voltage [V]", false);
		this.sumCurrentGraphController= new XY_GraphController(actualLoad.getLoadingDetails(), "Current", "Current [A]", false);
		
		this.sumEnergyGraph= sumEnergyGraphController.getView();
		this.sumVoltageGraph= sumVoltageGraphController.getView();
		this.sumCurrentGraph= sumCurrentGraphController.getView();
		
		this.energyGraphController= new XY_GraphController(actualLoad.getLoadingDetails(), "Energy", "Energy [kWh]", true);
		this.voltageGraphController= new XY_GraphController(actualLoad.getLoadingDetails(), "Voltage", "Voltage [V]", true);
		this.currentGraphController= new XY_GraphController(actualLoad.getLoadingDetails(), "Current", "Current [A]", true);
		
		this.energyGraph= energyGraphController.getView();
		this.voltageGraph= voltageGraphController.getView();
		this.currentGraph= currentGraphController.getView();
	}
	
	public void createNewGraphs(ActualLoad actualLoad){
		this.sumEnergyGraphController.newDataset(actualLoad.getLoadingDetails());
		this.sumVoltageGraphController.newDataset(actualLoad.getLoadingDetails());
		this.sumCurrentGraphController.newDataset(actualLoad.getLoadingDetails());
		
		this.energyGraphController.newDataset(actualLoad.getLoadingDetails());
		this.voltageGraphController.newDataset(actualLoad.getLoadingDetails());
		this.currentGraphController.newDataset(actualLoad.getLoadingDetails());
	}
	
	private void updateGraphs(ActualLoad actualLoad){
		this.sumEnergyGraphController.updateInfo(actualLoad.getLoadingDetails());
		this.sumVoltageGraphController.updateInfo(actualLoad.getLoadingDetails());
		this.sumCurrentGraphController.updateInfo(actualLoad.getLoadingDetails());
		
		this.energyGraphController.updateInfo(actualLoad.getLoadingDetails());
		this.voltageGraphController.updateInfo(actualLoad.getLoadingDetails());
		this.currentGraphController.updateInfo(actualLoad.getLoadingDetails());
	}
	
	
	public void updateGUI(ActualLoad load){
		if (load.isNewLoad()){
			createNewGraphs(load);
			this.chartView.addNewGraphs(sumEnergyGraph, sumVoltageGraph, sumCurrentGraph, energyGraph, sumVoltageGraph, currentGraph);
			load.setNewLoad(false);
		}
		else {
			updateGraphs(load);
		}
		//System.out.println("CMC uüdate Epres: " + load.getLoadingData().getEpres());
		this.sumLoadingInfoController.updateInfo(load);
		this.loadingInfoController.updateInfo(load);
		
	}
	
	
	
	/*
	 * 	MouseAdapter to enlarge tiles of a summary-view or minimize it afterwards
	 */
		class BoxListener extends MouseAdapter{
			
			public void mouseClicked(MouseEvent me)
	    	{
				JPanel clickedBox =(JPanel)me.getSource(); // get the reference to the box that was clicked 
				chartView.changeCurrentCard(clickedBox.getName());              
	        }
		}
}

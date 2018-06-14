package gui.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import gui.model.ServerConnectionState;
import gui.view.ChartView;
import gui.view.InfoView;
import gui.view.LoadingTableView;
import gui.view.MainView;
import gui.view.ServerConnectionView;
import gui.view.ServerStateView;
import gui.view.SettingsView;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import keba.rmiinterface.KEBADBInterface;
import keba.rmiinterface.KEBAFileInterface;
import keba.rmiinterface.KEBAInterface;
import keba.rmiinterface.RemoteObserver;

import remote.db.RemoteDatabase;

import dbObjects.ActualLoad;
import dbObjects.LoadingData;
import dbObjects.Report100;

public class MainController extends UnicastRemoteObject implements  RemoteObserver, Observer{
	private static final long serialVersionUID = 8026520513242744791L;

	private JFrame mainFrame;
	
	private ServerStateController serverStateController;
	private ServerStateView serverStateView;
	
	private ServerConnectionController serverConnectionController;
	private ServerConnectionView serverConnectionView;
	
	private ChartMainController chartMainController;
	private ChartView chartCard;
	
	private LoadingTableController loadingTableController;
	private LoadingTableView loadingCard;
	
	private InfoController infoController;
	private InfoView infoCard;
	
	private SettingController settingController;
	private SettingsView settingsCard;
	
	private KEBAInterface udpServerStub;
	private KEBADBInterface db;
	private KEBAFileInterface fileHandler;
	
	//private ServerSettings serverSettings;

	//private LoadingData loadingData;
	private Boolean isConnected;

	public MainController() throws RemoteException{
		super();
		//this.serverSettings= new ServerSettings();
		//setRemoteObjects();

		try {
			this.serverConnectionController= new ServerConnectionController();
			this.isConnected= this.serverConnectionController.isConnected();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.serverConnectionController.addObserver(this);
		
		updateAll();
		
		this.serverStateController= new ServerStateController(this.udpServerStub, this.isConnected);
		
		this.serverConnectionView= serverConnectionController.getView();
		this.serverStateView= serverStateController.getView();
		
		this.mainFrame= new MainView(chartCard, loadingCard, infoCard, settingsCard, 
				serverConnectionView, serverStateView);

		addWindowListener();
		showView();
	}
	
	private void updateAll(){
		//String actualLoadFilePath= null;
		//LoadingObject loading= null;
		
		if (this.isConnected){
			System.out.println("MC: isconnected true");
			this.udpServerStub= serverConnectionController.getServerStub();
			this.db= serverConnectionController.getDatabaseStub();
			this.fileHandler= serverConnectionController.getFileHandlerStub();
			
			try {
				this.udpServerStub.addRemoteObserver(this);
				//this.loadingData= this.db.getActualLoading();
				//actualLoadFilePath = loadingData.getLoadFilePath();
				//loading= fileHandler.getLoadingData(actualLoadFilePath);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.loadingTableController= new LoadingTableController(this.db);
			this.infoController= new InfoController();
			this.settingController= new SettingController();
			this.chartMainController= new ChartMainController(this.db, this.fileHandler, this.udpServerStub);
			
			this.chartCard= chartMainController.getChartView();
			this.loadingCard= loadingTableController.getLoadingView();
			this.infoCard= infoController.getInfoView();
			this.settingsCard= settingController.getSettingView();
			
			((MainView)this.mainFrame).setActualCard(chartCard);
			((MainView)this.mainFrame).setLoadingCard(loadingCard);
			((MainView)this.mainFrame).setInfoCard(infoCard);
			((MainView)this.mainFrame).setSettingsCard(settingsCard);
			
			addLoadings();
		}
		else{
			removeObserver();
			
			this.udpServerStub= null;
			this.db= null;
			this.fileHandler= null;
			
			this.loadingTableController= null;
			this.infoController= null;
			this.settingController= null;
			this.chartMainController= null;
			
			System.out.println("MC: isconnected false");
			this.chartCard= null;
			this.loadingCard= null;
			this.infoCard= null;
			this.settingsCard= null;
		}
	}
	
	private void changeView(Boolean isConnected){
		((MainView)this.mainFrame).changeLayoutConnection(isConnected);
	}
	
	public void addWindowListener(){
		WindowAdapter winAdapt= new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(mainFrame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	System.out.println("System Exit");
		            System.exit(0);
		            removeObserver();
		        }
		        else {
		        	// TODO do nothing
		        }
			}
		};
		((MainView) mainFrame).setWinListener(winAdapt);
	}
	
	public void removeObserver(){
		if (this.udpServerStub!= null){
			try {
				this.udpServerStub.deleteRemoteObserver(this);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void showView(){
        this.mainFrame.setVisible(true);
    }
	
	public void addLoadings(){
		try {
			ArrayList<LoadingData> loadings= RemoteDatabase.getInstance().getDB().getAllLoadings();
			this.loadingCard.add(loadings);
			System.out.println("Amount of loadings= " + loadings.size());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	// Remote Observer
	@Override
	public void update(Object observable, Object updateMsg)
			throws RemoteException {
		//System.out.println("MC Received Rem: updateMessage " + updateMsg);
		
		if (updateMsg instanceof ActualLoad){
			ActualLoad load= (ActualLoad)updateMsg;
			//Report100 loadData= load.getLoadingData();
			//System.out.println("MC Rem obs received ActualLoad for sessionid: " + loadData.getSessionID());
			//System.out.println("MC Rem obs received ActualLoad Epres: " + loadData.getEpres());
			/*System.out.println("GUIREADY= " + guiReady);
			System.out.println("chartMainController= " + chartMainController);
			System.out.println("load= " + load);*/
			if (chartMainController!=null) chartMainController.updateGUI(load);
		}
		else if (updateMsg instanceof Report100){
			System.out.println("MC Rem obs received Report100 ");
			//chartMainController.updateGUI();
		}
		else {
			//System.out.println("MC Rem obs received Boolean? = " + updateMsg.toString());
			serverStateController.isRunning((Boolean)updateMsg);
		}
	}

	
	// local observer "Connection State changes"
	@Override
	public void update(Observable obs, Object updateMsg) {
		System.out.println("updateMsg received= " + updateMsg.getClass());
		if(updateMsg instanceof ServerConnectionState){
			System.out.println("update ServerCOnnectionState " + ((ServerConnectionState)updateMsg).getIsConnected());
			
			this.isConnected= ((ServerConnectionState)updateMsg).getIsConnected();
			updateAll();
			changeView(isConnected);
			
			this.serverConnectionController.updateView();
			this.serverStateController.setUdpServerStub(this.udpServerStub);
			this.serverStateController.enableButton(isConnected);
		}
	}	
	
	
	/*private void setRemoteObjects(){
		String host = serverSettings.getHost();
        
		try {        	
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            
            String[] boundNames = registry.list();
            for (String name : boundNames)
            {
            	System.out.println("REGISTRY : " + name);
            }
           
            udpServerStub = (KEBAInterface) registry.lookup(KEBAInterface.SERVICE_NAME);            
            db = (KEBADBInterface) registry.lookup(KEBADBInterface.SERVICE_NAME);
            fileHandler = (KEBAFileInterface) registry.lookup(KEBAFileInterface.SERVICE_NAME);            
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            System.out.println("Server not started");
            //TODO Pop up with message "Server not started or reachable" + grey out button
            e.printStackTrace();
        }
	}*/


	/*// Observer
	@Override
	public void update(Observable observable, Object updateMsg) {
		System.out.println("MC Received 2: updateMessage " + updateMsg);
		System.out.println("MC Received 2: updateMessage " + updateMsg.getClass());
		System.out.println("MC Received 2: observable " + observable);
		System.out.println("MC Received 2: observable " + observable.getClass());
	}*/
}

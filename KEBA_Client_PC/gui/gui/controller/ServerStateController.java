package gui.controller;

import gui.model.ServerStateObservable;
import gui.view.ServerStateView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import keba.rmiinterface.KEBAInterface;

public class ServerStateController extends UnicastRemoteObject {
	private static final long serialVersionUID = 1L;

	private ServerStateView serverView;
	
	private ServerStateObservable isRunning;
	private KEBAInterface udpServerStub;
	
	//private Boolean isConnected;
	
	protected ServerStateController(KEBAInterface udpServerStub, Boolean isConnected) throws RemoteException{
		super();
		//this.isConnected= isConnected;
		this.serverView= new ServerStateView(isConnected);
		this.udpServerStub= udpServerStub;
		this.isRunning= new ServerStateObservable(false);
		//this.isRunning.addObserver(this);
		
		if (isConnected){
			try {
				this.isRunning.setRunning(this.udpServerStub.getServerState());
			} catch (RemoteException e) {
				this.isRunning.setRunning(false);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		addListener();
		updateView();
	}
	
	private void checkServerState(Boolean isConnected){
		if (isConnected){
			try {
				this.isRunning.setRunning(this.udpServerStub.getServerState());
			} catch (RemoteException e) {
				this.isRunning.setRunning(false);
				e.printStackTrace();
			}
		}
	}
	
	public void setUdpServerStub(KEBAInterface udpServerStub){
		this.udpServerStub= udpServerStub;
	}

	public void showView(){
        this.serverView.setVisible(true);
    }
	
	public ServerStateView getView(){
		return this.serverView;
	}
	
	
	
	/*
	 * Remote Observer handling
	 */


	private void addListener() {
		this.serverView.setServerStateListener(new ServerStateListener());
	}
	
	class ServerStateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Received input from server!");
			if (isRunning.getisRunning()){
				try {
					udpServerStub.stopServer();
					Thread.sleep(500);
					isRunning.setRunning(udpServerStub.getServerState());
				} catch (RemoteException | InterruptedException e1) {
					System.out.println("Error in Client: " + e1);
				}
			}
			else{
				try {
					udpServerStub.startServer();
					Thread.sleep(500);
					isRunning.setRunning(udpServerStub.getServerState());
				} catch (RemoteException | InterruptedException e1) {
					System.out.println("Error in Client: " + e1);
				}
			}
		}
	}
	
	public void updateView(){
		
		this.serverView.setServerState(isRunning.getisRunning());
	}
	
	public void enableButton(Boolean isConnected){
		this.serverView.greyOut(isConnected);
		System.out.println("updateView before checkServerState() = " + isRunning.getisRunning());
		checkServerState(isConnected);
		System.out.println("updateView after checkServerState() = " + isRunning.getisRunning());
		updateView();
	}
	
	public void isRunning(Boolean isRunning){
		this.isRunning.setRunning(isRunning);
		updateView();
	}

	/*	// Observer
	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("SSC Received input from server! 2");
		
		System.out.println("SSC Received ServState: updateMessage " + arg1);
		System.out.println("SSC Received ServState: observable " + arg0);
		System.out.println("SSC Received ServState: observable " + arg0.getClass());
		
		if (arg1 instanceof ActualLoad){
			System.out.println("ACTUALLOAD Rem obs received ActualLoad for sessionid: " );
			ActualLoad load= (ActualLoad)arg1;
			Report100 loadData= load.getLoadingData();
			System.out.println("Rem obs received ActualLoad for sessionid: " + loadData.getSessionID());
		}
		else {
			if (arg1== null) arg1= false;
			System.out.println("DEFAULT Rem obs received Boolean? = " + arg1.toString());
			//isRunning.setRunning((Boolean)arg1);
			updateView();
		}
	}

	// Remote Observer
	@Override
	public void update(Object observable, Object updateMsg)
			throws RemoteException {
		System.out.println("SSC Received Rem ServState: updateMessage " + updateMsg);
		System.out.println("SSC Received Rem ServState: observable " + observable);
		System.out.println("SSC Received Rem ServState: observable name " + observable.getClass().getName());
		
		if (updateMsg instanceof ActualLoad){
			System.out.println("ACTUALLOAD Rem obs received ActualLoad for sessionid: " );
			ActualLoad load= (ActualLoad)updateMsg;
			Report100 loadData= load.getLoadingData();
			System.out.println("Rem obs received ActualLoad for sessionid: " + loadData.getSessionID());
		}
		else {
			System.out.println("DEFAULT Rem obs received Boolean? = " + updateMsg.toString());
			isRunning.setRunning((Boolean)updateMsg);
			updateView();
		}
		
	}*/
	
	

}

package gui.controller;

import gui.model.KEBAClientSettings;
import gui.model.ServerConnectionState;
import gui.view.ServerConnectionView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Observable;

import keba.rmiinterface.KEBADBInterface;
import keba.rmiinterface.KEBAFileInterface;
import keba.rmiinterface.KEBAInterface;

public class ServerConnectionController extends Observable {

	private ServerConnectionView serverConnectionView;
	
	private KEBAInterface udpServerStub;
	private KEBADBInterface db;
	private KEBAFileInterface fileHandler;
	
	private KEBAClientSettings serverSettings;
	private ServerConnectionState isConnected;
	
	protected ServerConnectionController() throws RemoteException{
		super();
		this.serverConnectionView= new ServerConnectionView();
		this.serverSettings= KEBAClientSettings.getInstance();
		isConnected= new ServerConnectionState();
		isConnected.setIsConnected(false);
		
		addListener();
		showView();
		updateView();
	}

	public void showView(){
        this.serverConnectionView.setVisible(true);
    }
	
	public ServerConnectionView getView(){
		return this.serverConnectionView;
	}
	
	public Boolean isConnected(){
		return isConnected.getIsConnected();
	}
	
	
	private void connectToServer(){
		String host = serverSettings.HOSTNAME;
        
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
            isConnected.setIsConnected(true);
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            System.out.println("Server not started");
            isConnected.setIsConnected(false);
            //TODO Pop up with message "Server not started or reachable" + grey out button
            e.printStackTrace();
        }
	}
	
	public KEBAInterface getServerStub(){
		return this.udpServerStub;
	}
	
	public KEBADBInterface getDatabaseStub(){
		return this.db;
	}
	
	public KEBAFileInterface getFileHandlerStub(){
		return this.fileHandler;
	}
	
	
	
	private void addListener() {
		this.serverConnectionView.setServerConnectionListener(new ServerConnectionListener());
	}
	
	class ServerConnectionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isConnected.getIsConnected()){
				isConnected.setIsConnected(false);
				setChanged();
				notifyObservers(isConnected);
				System.out.println("Disconnected from Server= " + isConnected);
			}
			else {
				connectToServer();
				setChanged();
				notifyObservers(isConnected);
				System.out.println("Connected to Server= " + isConnected.getIsConnected());
			}
		}
	}

	public void updateView(){
		this.serverConnectionView.setConnectionState(isConnected.getIsConnected());
	}
}

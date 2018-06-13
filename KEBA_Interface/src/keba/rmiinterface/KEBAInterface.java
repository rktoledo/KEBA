package keba.rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dbObjects.Report100;

public interface KEBAInterface extends Remote{

	public static final String SERVICE_NAME = "KEBAInterface";
	
	void startServer() throws RemoteException;
	void stopServer() throws RemoteException;
	boolean getServerState() throws RemoteException;
	Report100 report100() throws RemoteException;
	Report100 report(String rep) throws RemoteException;
	
	public boolean addRemoteObserver(RemoteObserver obs) throws RemoteException;
	void deleteRemoteObserver(RemoteObserver obs) throws RemoteException;
}

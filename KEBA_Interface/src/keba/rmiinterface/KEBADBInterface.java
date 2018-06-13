package keba.rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import dbObjects.LoadingData;
import dbObjects.LoadingObject;

public interface KEBADBInterface extends Remote{
	
	public static final String SERVICE_NAME = "KEBADBInterface";
	
	LoadingData getLoadingObject(int sessionID) throws RemoteException;
	ArrayList<LoadingData> getAllLoadings() throws RemoteException;
	ArrayList<LoadingData> getLoadingsFrom(int loadID) throws RemoteException;
	ArrayList<LoadingData> getLoadingsTo(int loadID) throws RemoteException;
	ArrayList<LoadingData> getLoadingsFromTo(int loadIDstart, int loadIDend) throws RemoteException;
	LoadingData getLoading(int loadiID) throws RemoteException;
	
	LoadingData getActualLoading() throws RemoteException;
}

package keba.rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.json.simple.JSONObject;

import dbObjects.LoadingObject;

public interface KEBAFileInterface extends Remote{
	
	public static final String SERVICE_NAME = "KEBAFileInterface";
	
	JSONObject getLoadingFileRem(String filepath) throws RemoteException;
	LoadingObject getLoadingData(String filepath) throws RemoteException;
	
	JSONObject getStateFileRem(String filepath) throws RemoteException;
}

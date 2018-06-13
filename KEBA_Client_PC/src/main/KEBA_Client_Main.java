package main;

import java.rmi.RemoteException;

import gui.controller.MainController;

import keba.rmiinterface.KEBADBInterface;
import keba.rmiinterface.KEBAFileInterface;
import keba.rmiinterface.KEBAInterface;


public class KEBA_Client_Main{
	
	KEBADBInterface db;
	KEBAFileInterface fileHandler;
	KEBAInterface kebaIF;

	public static void main(String[] args) {
		MainController controller;
		try {
			controller = new MainController();
			controller.showView();
			//controller.addLoadings();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package kebaserver.main;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import dbObjects.Report100;

import keba.rmiinterface.KEBAInterface;
import keba.rmiinterface.RemoteObserver;

import udpServer.KEBAUDPServerThread;

public class KEBA extends Observable implements KEBAInterface, Observer {
	
	private KEBAUDPServerThread udpThread;
	
	public KEBA (){
    	udpThread= new KEBAUDPServerThread();
    	udpThread.addObserver(this);
    }
	
	@Override
	public void startServer() throws RemoteException {
		udpThread.startUDPTask();
	}

	@Override
	public void stopServer() throws RemoteException {
		udpThread.stopUDPTask();
	}

	@Override
	public boolean getServerState() throws RemoteException {
		return udpThread.getServerState();
	}
	
	@Override
	public Report100 report100() throws RemoteException {
		return udpThread.report100();
	}

	@Override
	public Report100 report(String rep) throws RemoteException {
		return udpThread.reportI(rep);
	}

	
	
	
	private ArrayList<RemoteObserver> observers= new ArrayList<RemoteObserver>();

	@Override
	public boolean addRemoteObserver(RemoteObserver obs) throws RemoteException {
		WrappedObserver mo = new WrappedObserver(obs);
        addObserver(mo);
        observers.add(obs);
        System.out.println("Added observer:" + obs.getClass());
        System.out.println("obs = " + obs.toString());
        System.out.println("observers size= " + observers.size());
        return true;
	}
	
	@Override
    public void deleteRemoteObserver (RemoteObserver obs) throws RemoteException{
    	observers.remove(obs);
    	System.out.println("observers size= " + observers.size());
    }
	
	private class WrappedObserver implements Observer, Serializable {

		private static final long serialVersionUID = 1L;
		private RemoteObserver ro = null;

        public WrappedObserver(RemoteObserver ro) {
            this.ro = ro;
        }
        

        @Override
        public void update(Observable o, Object arg) {
            try {
            	//System.out
                //.println("REMOTE OBSERVER Serv update calls " + arg.getClass());
                ro.update(o, arg);
                //System.out
                //.println("REMOTE OBSERVER Serv update calls 1" + arg.getClass());
            } catch (RemoteException e) {
                System.out
                        .println("Remote exception removing observer:" + this);
                e.printStackTrace();
                o.deleteObserver(this);
            }
        }		

    }

	@Override
	public void update(Observable arg0, Object arg1) {
		setChanged();
		//System.out.println("OBSERVER Serv update " + arg1.getClass());
		notifyObservers(arg1);
	}

	

	

}

package kebaserver.main;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import dbMySQL.MySqlDb;
import fileHandling.FileHandler;

import keba.rmiinterface.KEBAInterface;
import keba.rmiinterface.KEBADBInterface;
import keba.rmiinterface.KEBAFileInterface;

import networkSettings.ServerNetworkInformation;

public class KEBAServer_Main{
	
	public KEBAServer_Main(){
		super();
	}
         
    public static void main(String[] args) throws Exception {
    	ServerNetworkInformation serverNetworkInfo= new ServerNetworkInformation();
		int port= 1099;
        
        try {
        	KEBA keba= new KEBA();
        	MySqlDb db= new MySqlDb();
        	FileHandler fileHandler = FileHandler.getInstance();
            
            String ip= serverNetworkInfo.getServerIP();
            System.out.println("Server on IP: " + ip);
            
            System.setProperty("java.rmi.server.hostname","RSK");
            
            KEBAInterface kebaInterfaceStub= (KEBAInterface) UnicastRemoteObject.exportObject(keba, 0);
            KEBADBInterface kebaDBInterfaceStub= (KEBADBInterface) UnicastRemoteObject.exportObject(db, 0);
            KEBAFileInterface kebaFileInterfaceStub= (KEBAFileInterface) UnicastRemoteObject.exportObject(fileHandler, 0);
            
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(port);
            if (registry==null)
            {
             System.out.println("getRegistry");
             registry = java.rmi.registry.LocateRegistry.getRegistry(port);
            }
           
            registry.rebind(KEBAInterface.SERVICE_NAME, kebaInterfaceStub);
            registry.rebind(KEBADBInterface.SERVICE_NAME, kebaDBInterfaceStub);
            registry.rebind(KEBAFileInterface.SERVICE_NAME, kebaFileInterfaceStub);
            
            String[] boundNames = registry.list();
            System.out.println("REGISTRY: " + registry.toString());
            for (String name : boundNames)
            {
            System.out.println("REGISTRY : " + name);
            }
            
            System.out.println("CODEBASE: " + System.getProperty("java.rmi.server.codebase"));
            // registry.bind("KEBA_UDPServer", udpServerStub);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
}

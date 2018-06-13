package kebaserver.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import db.DbDAOFactory;
import dbDAOInterface.DbDAO;
import dbObjects.Report100;

public class Test {
	public static void main(String[] args) {
		Report100 reportI= new Report100();
    	DatagramSocket udpSocket2;
    	DbDAO database= DbDAOFactory.getDbDAOFactory("mysql").getDbDAO();
    	
    	try {
        	InetAddress IPAddress = InetAddress.getByName("192.168.1.33");
        	byte[] sendData = new byte[512];
        	byte[] receiveData = new byte[512];
        	udpSocket2 = new DatagramSocket(7090);
        	
        	for (int i= 3; i<31; i++){
        		String help= "0";
        		String reportnumber;
        		if (i<10){
        			reportnumber= help + i;
        		}
        		else {
        			reportnumber= ""+ i;
        		}
        		String sentence = "report 1" + reportnumber;
        		sendData = sentence.getBytes();
        		
        		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 7090);
        		udpSocket2.send(sendPacket);
        		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        		udpSocket2.receive(receivePacket);
        	
        		String modifiedSentence = new String(receivePacket.getData());
        	
        		reportI.createFromString(modifiedSentence);
        		System.out.println("ReportI new ID= " + reportI.getSessionID());
        		database.createLoading(reportI.getStarted(), reportI.getSessionID());
        		database.terminateLoading(reportI.getEnded(), null, null, reportI.getEpres());
        	}
        	udpSocket2.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	public static void reportI(String reportnumber){
    	
    	
    }
	
}

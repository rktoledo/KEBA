package udpServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import serverobjects.Report2;
import serverobjects.Report3;

import db.DbDAOFactory;
import dbDAOInterface.DbDAO;
import dbObjects.ActualLoad;
import dbObjects.LoadingData;
import dbObjects.LoadingObject;
import dbObjects.Report100;

import fileHandling.FileHandler;

public class KEBAUDPServerThread extends Observable implements Runnable{
	
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private Boolean schedulerIsRunning= false;
	
	private DatagramSocket udpSocket;
	private DatagramSocket udpSocket2;
    private static int port= 7092;
    private static int port2= 7090;
    private static String WallboxIP= "192.168.1.33";
    private int clientPollTime= 2000;	

    private LocalDateTime ldt;
    
    //private String loadingLogFile= null;
    private JSONObject loadingLog= new JSONObject();
    private JSONArray loadingLogTime= new JSONArray();
    private JSONArray loadingLogPower= new JSONArray();
    private JSONArray loadingLogVoltage= new JSONArray();
    private JSONArray loadingLogCurrent= new JSONArray();
    
    //private String stateLogFile= null;
    private JSONObject stateLog= new JSONObject();
    private JSONArray stateLogTime= new JSONArray();
    private JSONArray stateLogEvent= new JSONArray();
    
    private String sessionStart= null;
    private boolean fileOpen= false;
    private boolean isRunning= false;
    private boolean loadOngoing= false;
    private int stateValueCount= 0;
    private int loadingValueCount= 0;
    private int countChanges= 0;
    
    private Report2 report2= new Report2();
    private Report3 report3= new Report3();
    private Report100 report100= new Report100();

    private DbDAO database;
    //private int currentLoadID;
    
    private LoadingData load;
    private LoadingObject loadObj;
    private ActualLoad actualLoad;
    
    private Thread UDPServerThread;
    
    private Timer timer;
    
    public KEBAUDPServerThread (){	
    	actualLoad= new ActualLoad();
    	loadObj= new LoadingObject();
    	database= DbDAOFactory.getDbDAOFactory("mysql").getDbDAO();
    }
 
    /*
     * Start the UDP Server
     */
    public void startUDPTask() {
    	if(isRunning){
    		setChanged();
    		notifyObservers(isRunning);
    	}
    	else {
    		UDPServerThread = new Thread(this);
    		UDPServerThread.start();
    	}
    }
    
    private void startPollObservers(){
    	System.out.println("Start polling");
    	
    	timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            	loadObj= new LoadingObject(loadingLog);
            	loadObj.setSessionID(report100.getSessionID());
            	actualLoad.setLoadingData(report100);
            	actualLoad.setLoadingDetails(loadObj);
            	//System.out.println("NOT OBS 3" + actualLoad.getLoadingData().getSessionID());
            	setChanged(); 
				notifyObservers(actualLoad);
				}
        }, 0, clientPollTime);
    }

    
    /*
     * STOP UDP Server
     */
    @SuppressWarnings("unchecked")
	public void stopUDPTask() {
      // Interrupt the thread so it unblocks any blocking call
    	if (UDPServerThread.isInterrupted()) {
    		if (!this.udpSocket.isClosed()){
    			this.udpSocket.close();
    		}
    		ldt= LocalDateTime.now();
    		stateLogTime.add(ldt.toString());
    		stateLogEvent.add("Server stopped");
			saveFiles(1);
    		System.out.println("Server : isInterrupted ");
    		setChanged();
      	  	notifyObservers(isRunning);
  		}
    	
    	else {
    		isRunning= false;
    		UDPServerThread.interrupt();
    		timer.cancel();
    		//if (SimpleTimer.isRunning()) SimpleTimer.stop();
    	}
    	
      // Wait until the thread exits
      try {
    	  //System.out.println("Server : Before Join ");
    	  UDPServerThread.join(2000);
    	  System.out.println("Server : Stopped successfully");
    	  //System.out.println("Server : After Join ");
      } catch (InterruptedException ex) {
        // Unexpected interruption
    	  System.out.println("Error : " + ex);
    	  ex.printStackTrace();
    	  System.exit(1);
      }
    }

    public void run() {
    	isRunning= true;
    	load= new LoadingData();
		//setChanged();
		//notifyObservers(isRunning);
		
		report2();
		report3();
		report100();
		
		load.setLoadID(database.getCurrentLoadingID(report100.getSessionID()));		
		
		
		if (load.getLoadID() != 0){	
			getExistingFiles();			
			loadOngoing= true;
		}
		else {
			ldt = LocalDateTime.now();
			
			load= new LoadingData();
			
    		createFiles(true);
    		createNewCharge();
			
    		loadOngoing= true;
    		
    		System.out.println("CurrentLoadID= " + load.getLoadID());
    		System.out.println("latestSessionID= " + report100.getSessionID());
    		
		}
		
		loadObj= new LoadingObject(loadingLog);
    	System.out.println("setActualLoad  Both (run)");
    	actualLoad.setLoadingData(report100);
    	loadObj.setSessionID(report100.getSessionID());
    	actualLoad.setLoadingDetails(loadObj);
    	
    	System.out.println("NOT OBS 4" + actualLoad.getLoadingData().getSessionID());
		setChanged();
		notifyObservers(actualLoad);
		
		startPollObservers();
    	
      while (isRunning) {
    	  
    	  System.out.println("UDP Server running");
        try {
			this.udpSocket = new DatagramSocket(port);
			listen();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			stopUDPTask();
		}
      }
      System.out.println("UDP Server stopped 2");
    }
    
    private void listen() throws Exception {
        String msg;
        System.out.println("Server running");
        
        if (fileOpen==false) {
        	ldt = LocalDateTime.now();
        	load= new LoadingData();
    		createFiles(true);
    		loadOngoing= true;
    	}
        
        System.out.println("Used loadFile= " + load.getLoadFilePath());
        System.out.println("Used staetFile= " + load.getStateFilePath());
        
        while (isRunning) {
            
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // blocks until a packet is received
            try{
            	udpSocket.receive(packet);
            	
            } catch (SocketException ex) {
            	if (udpSocket.isClosed()){
            		saveFiles(1);
            		isRunning= false;
            		setChanged();
            		notifyObservers(isRunning);
            		System.out.println("Socket closed");
            	}
            	else {
            		System.out.println("Server Socket Exception" + ex);
            		ex.printStackTrace();
            	}
            	break;
            }
            msg = new String(packet.getData()).trim();
            msg= msg.substring(1, msg.length()-1);
            //showMessageReceived(msg);
            if (packet.getAddress().getHostAddress().equals(WallboxIP)) {
            	ldt = LocalDateTime.now();
            	parseString(msg);
            }
            else System.out.println("InorrectIP of Wallbox : "+ packet.getAddress().getHostAddress());
        }
    }
    

    
    @SuppressWarnings("unchecked")
	private void parseString(String msg) {
    	String[] receivedMsg;
    	receivedMsg= msg.split(":");
    	String msgArgument= receivedMsg[0];
    	String msgValue= receivedMsg[1];
    	msgValue= msgValue.replaceAll("\\s+","");
    	//System.out.println(msgArgument+ " " + msgValue);
    	//System.out.println(msgValue);
    	
    	switch (msgArgument) {
    	
    	// State = Current state of the charging station
    	case "\"State\"":
    		switch (Integer.parseInt(msgValue.trim())) {
    		// starting
    		case 0:
    			System.out.println("Starting state 0");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Starting");
    			break;
    		// not ready for charging: e.g. unplugged, X1  or ena not enabled, RFID not enabled
    			/*
    			 * TERMINATE A LOAD alternative
    			 */
    		case 1:
    			report2();
    			report100();
    			System.out.println("report2.getPlug= " + report2.getPlug());
    			if (report2.getPlug() == 3 && loadOngoing== true){
    				System.out.println("Unplugged on EV");
        			stateLogTime.add(ldt.toString());
            		stateLogEvent.add("Unplugged on EV");
        			database.terminateLoading(ldt, load.getLoadFilePath(), load.getStateFilePath(), report100.getEpres());
        			
        			actualLoad.setComplete(true);
        			saveFiles(1);
        			actualLoad.setComplete(false);
        			
        			report100();
        			database.setSessionID(load.getLoadID(), report100.getSessionID());
        			
        			loadOngoing= false;
    			}
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Not ready");
    			break;
    		// ready for charging: Waiting for EV charging request (S2) - NEW LOADING SESSION!
    		case 2:
    			System.out.println("Plugged and ready for charging");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Ready - waiting for EV");
    			break;
    		// charging
    		case 3:
    			System.out.println("Charging");
    			report100();
    			
    			database.setSessionID(load.getLoadID(), report100.getSessionID());
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Charging");
    			break;
    		// error
    		case 4:
    			System.out.println("Error");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Error");
    			break;
    		// Authorization rejected
    		case 5:
    			System.out.println("Auth rejected");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Authorisation rejected");
        		/* TODO Check why it end in this state after Ena=0
        		stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Unplugged on EV");
    			database.terminateLoading(ldt, loadingLogFile, stateLogFile);
    			saveFiles(1);
    			*/
        		//TODO End of lines t be removed
    			break;
    		default:
    			System.out.println("State default");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Unknown state");
    			//"Not possible currently"
        		break;
    		}
    		stateValueCount++;
    		break;
    	// Current condition of loading connection
    	case "\"Plug\"":
    		switch (Integer.parseInt(msgValue.trim())) {
    		// unplugged
    		case 0:		// Not used in stations with fixed cables
    			System.out.println("Unplugged on Wallbox");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Unplugged on Wallbox");
    			break;
    		// plugged on charging station
    		case 1: // Not used in stations with fixed cables
    			System.out.println("Plugged on Wallbox");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Plugged on Wallbox");
    			break;
    		// plugged on charging station 		plug locked
    			/*
    			 * TERMINATE A LOAD
    			 */
    		case 3:
    			System.out.println("Unplugged on EV");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Unplugged on EV");
        		report2();
    			report100();
    			database.terminateLoading(ldt, load.getLoadFilePath(), load.getStateFilePath(), report100.getEpres());
    			
    			actualLoad.setComplete(true);
    			saveFiles(1);
    			actualLoad.setComplete(false);
    			
    			report100();
    			database.setSessionID(load.getLoadID(), report100.getSessionID());
    			
    			loadOngoing= false;
    			break;
    		// plugged on charging station 							plugged on EV
    		case 5: // Not used in stations with fixed cables
    			System.out.println("Plug 5");
    			break;
    		// plugged on charging station 		plug locked			plugged on EV
    			/*
    			 * START A LOAD
    			 */
    		case 7:
    			System.out.println("Plugged on EV");
    			if (fileOpen) {
    				saveFiles(1);
    			}
    			
    			load= new LoadingData();
    			actualLoad.setNewLoad(true);
    			createFiles(false);
    			
    			loadOngoing= true;
    			actualLoad.setNewLoad(false);
 
        		createNewCharge();
    
    			schedulerIsRunning= true;
    			scheduler.schedule(new Runnable() {
    	            @Override
    	            public void run() {
    	                createNewCharge();
    	                System.out.println("Out of time!");
    	            }}, 500, TimeUnit.MILLISECONDS);
    			
    			break;
    		default:
    			System.out.println("Default");
    			stateLogTime.add(ldt.toString());
        		stateLogEvent.add("Unknown plug state");
    			// currently not possible
        		break;
    		}
    		stateValueCount++;
    		break;
    	// State of the potential free Enable input X1
    	case "\"Input\"":
    		stateLogTime.add(ldt.toString());
    		if (msgValue.equals("1")){stateLogEvent.add("External Input ON");}
    		else {stateLogEvent.add("External Input OFF");}
    		stateValueCount++;
    		break;
    	// Enable state for charging (contains Enable input, RFID, UDP, ...)
    	case "\"Enable sys\"":
    		stateLogTime.add(ldt.toString());
    		if (msgValue.equals("1")){stateLogEvent.add("Enabled per command");}
    		else {stateLogEvent.add("Disbaled per command");}
    		stateValueCount++;
    		break;
    	// Current preset value via Control pilot in Milliampere
    	case "\"Max curr\"":
    		stateLogTime.add(ldt.toString());
    		stateLogEvent.add("Max Current Set to: " + msgValue);
    		stateValueCount++;
    		break;
    	// Power consumption of the current loading session in 0.1kWh. Resets with "State=2"
    	case "\"E pres\"":
    		loadingLogTime.add(ldt.toString());
    		loadingLogPower.add(Integer.parseInt(msgValue.trim()));
    		report3();
    		loadingLogVoltage.add(report3.getU1());
    		loadingLogCurrent.add(report3.getI1());
    		loadingValueCount++;
    		//System.out.println(jsonObject);
    		break;
    	default:
    		// Log to unexpected message received logfile
    		stateLogTime.add(ldt.toString());
    		stateLogEvent.add("Unknown Command received _" + msgArgument + "_" + msgValue + "_");
    		System.out.println("other message received: " + msgArgument);
    	break;
    	}
    	
    	countChanges++;

    	if (countChanges>3){
    		report100();
			//database.setSessionID(load.getLoadID(), report100.getSessionID());
			countChanges= 0;
    	}
    	
    	if (loadingValueCount > 3) {
    		System.out.println("loadCount>3");
    		updateLoadingFile(1);
    		loadingValueCount= 0;
    	}
    	if (stateValueCount > 9){
    		updateStateFile();
    		stateValueCount= 0;
    	}
    	
    	//System.out.println("loadingValueCount= " + loadingValueCount);
    	//System.out.println("stateValueCount= " + stateValueCount);
    }
    
    private void createNewCharge(){
    	report100();
		load.setSessionID(report100.getSessionID());
		load.setStartDateTime(report100.getStarted());
		
    	if(!database.checkIfSessionIDExists(report100.getSessionID())){
    		//CREATE A NEW CHARGING ENTRY
    		load.setLoadID(database.createLoading(load.getStartDateTime()));
    		database.setSessionID(load.getLoadID(), load.getSessionID());
    		System.out.println("Loadfilepath added to DB= " + load.getLoadFilePath());
			database.addLoadingFilePath(load.getLoadFilePath());
			System.out.println("Statefilepath added to DB= " + load.getStateFilePath());
			database.addStateFilePath(load.getStateFilePath());
    	}
    	if (schedulerIsRunning){
    		scheduler.shutdown();
    		schedulerIsRunning= false;
    	}
    }
    
    @SuppressWarnings("unchecked")
	private void createFiles(boolean serverStarted) {
    	report100();
    	load.setStartDateTime(report100.getStarted());
    	ldt = LocalDateTime.now();
    	sessionStart= dateToFileName(load.getStartDateTime());
    	loadingLog= new JSONObject();
    	loadingLogTime= new JSONArray();
		loadingLogTime.add(load.getStartDateTime().toString());
		loadingLog.put("date", loadingLogTime);
		loadingLogPower= new JSONArray();
		loadingLogPower.add(report100.getEpres());
		loadingLog.put("value", loadingLogPower);
		loadingLogVoltage= new JSONArray();
		loadingLogVoltage.add(0);
		loadingLog.put("voltage", loadingLogVoltage);
		loadingLogCurrent= new JSONArray();
		loadingLogCurrent.add(0);
		loadingLog.put("current", loadingLogCurrent);
		System.out.println("New Object created LoadingLog");
		stateLog= new JSONObject();
		stateLogTime= new JSONArray();
		stateLogTime.add(ldt.toString());
		stateLog.put("date", stateLogTime);
		stateLogEvent= new JSONArray();
		if (serverStarted) stateLogEvent.add("Server started");
		else stateLogEvent.add("Plugged on EV");
		stateLog.put("state/plug", stateLogEvent);
		System.out.println("New Object created StateLog");
		
		createLoadFile();
		createStateFile();
		
		loadObj= new LoadingObject(loadingLog);
		loadObj.setSessionID(report100.getSessionID());
		actualLoad.setLoadingData(report100);
		actualLoad.setLoadingDetails(loadObj);
		
		setChanged();
		notifyObservers(actualLoad);
	
		fileOpen= true;
    }
    
    private void createLoadFile(){
    	ldt = LocalDateTime.now();
    	sessionStart= dateToFileName(report100.getStarted());
    	FileHandler fileHandler= FileHandler.getInstance();
    	load.setLoadFilePath(fileHandler.generateFile("load"+sessionStart, "loading"));
    	System.out.println("Loadfilepath created= " + load.getLoadFilePath());
    	fileHandler.writeJsontoFile(loadingLog, load.getLoadFilePath());
    }
    
    private void createStateFile(){
    	ldt = LocalDateTime.now();
    	sessionStart= dateToFileName(report100.getStarted());
    	FileHandler fileHandler= FileHandler.getInstance();
    	load.setStateFilePath(fileHandler.generateFile("state"+sessionStart, "state"));
    	System.out.println("Statefilepath created= " + load.getStateFilePath());
    	fileHandler.writeJsontoFile(stateLog, load.getStateFilePath());
    }
    
	private void getExistingFiles(){
    	FileHandler fileHandler= FileHandler.getInstance();
    	load.setLoadFilePath(database.getLoadingFilePath(load.getLoadID()));
    	if (load.getLoadFilePath()== null){
    		createFiles(true);
    		database.addLoadingFilePath(load.getLoadFilePath());
    	}
    	else {
    		loadingLog= fileHandler.getJsonFromFile(load.getLoadFilePath());
        	loadingLogTime= (JSONArray)loadingLog.get("date");
        	loadingLogPower= (JSONArray)loadingLog.get("value"); 
        	loadingLogVoltage= (JSONArray)loadingLog.get("voltage"); 
        	loadingLogCurrent= (JSONArray)loadingLog.get("current"); 
        	fileOpen= true;
    	}
		
    	load.setStateFilePath(database.getStateFilePath(load.getLoadID()));
    	if (load.getStateFilePath()== null){
    		createFiles(true);
    		database.addStateFilePath(load.getStateFilePath());
    	}
    	else {    	
    		stateLog= fileHandler.getJsonFromFile(load.getStateFilePath());
    		stateLogTime= (JSONArray)stateLog.get("date");
    		stateLogEvent= (JSONArray)stateLog.get("state/plug");
    		fileOpen= true;
    	}
    }
    
	@SuppressWarnings("unchecked")
	private void updateLoadingFile(int phases){
		System.out.println("File = " + load.getLoadFilePath());
    	FileHandler fileHandler= FileHandler.getInstance();
    	JSONObject loadLogTemp= fileHandler.getJsonFromFile(load.getLoadFilePath());
    	System.out.println("loadLogTemp= " + loadLogTemp);
    	if (loadLogTemp != null) {
    		loadLogTemp.put("date", loadingLogTime);
        	loadLogTemp.put("value", loadingLogPower);
        	loadLogTemp.put("voltage", loadingLogVoltage);
        	loadLogTemp.put("current", loadingLogCurrent);
    	}
     	
    	loadingLog= loadLogTemp;
    	
    	fileHandler.overwriteFile(loadingLog, load.getLoadFilePath());
    	
    	loadObj= new LoadingObject(loadingLog);
    	loadObj.setSessionID(report100.getSessionID());
    	System.out.println("setActualLoad Both (updateLoadingFile)");
    	actualLoad.setLoadingDetails(loadObj);
    	actualLoad.setLoadingData(report100);
    	//System.out.println("NOT OBS 2" + actualLoad.getLoadingData().getSessionID());
        
		setChanged();
		notifyObservers(actualLoad);
		}
	
	@SuppressWarnings("unchecked")
	private void updateStateFile(){
    	FileHandler fileHandler= FileHandler.getInstance();
    	JSONObject stateLogTemp= fileHandler.getJsonFromFile(load.getStateFilePath());
    
    	stateLogTemp.put("date", stateLogTime);
    	stateLogTemp.put("state/plug", stateLogEvent);
    	
    	stateLog= stateLogTemp;
    	fileHandler.overwriteFile(stateLog, load.getStateFilePath());
    }
	
	private void saveFiles(int phases){
		updateLoadingFile(phases);
		updateStateFile();
		fileOpen= false;
		loadingValueCount= 0;
		stateValueCount= 0;
	}
    
    private String dateToFileName (LocalDateTime ldt) {
    	Integer ldtYear= ldt.getYear();
    	String ldtYearStr= Integer.toString(ldtYear);
    	Integer ldtMonth= ldt.getMonthValue();
    	String ldtMonthStr= Integer.toString(ldtMonth);
    	if (ldtMonthStr.length()<2) ldtMonthStr= "0"+ldtMonthStr;
    	Integer ldtDay= ldt.getDayOfMonth();
    	String ldtDayStr= Integer.toString(ldtDay);
    	if (ldtDayStr.length()<2) ldtDayStr= "0"+ldtDayStr;
    	Integer ldtHour= ldt.getHour();
    	String ldtHourStr= Integer.toString(ldtHour);
    	if (ldtHourStr.length()<2) ldtHourStr= "0"+ldtHourStr;
    	if (ldtHourStr.length()<2) ldtHourStr= "0"+ldtHourStr;
    	Integer ldtMinute= ldt.getMinute();
    	String ldtMinuteStr= Integer.toString(ldtMinute);
    	if (ldtMinuteStr.length()<2) ldtMinuteStr= "0"+ldtMinuteStr;
    	if (ldtMinuteStr.length()<2) ldtMinuteStr= "0"+ldtMinuteStr;
    	Integer ldtSeconds= ldt.getSecond();
    	String ldtSecondsStr= Integer.toString(ldtSeconds);
    	if (ldtSecondsStr.length()<2) ldtSecondsStr= "0"+ldtSecondsStr;
    	if (ldtSecondsStr.length()<2) ldtSecondsStr= "0"+ldtSecondsStr;
    	String fileName= ldtYearStr+ ldtMonthStr+ ldtDayStr+ ldtHourStr+ ldtMinuteStr+ ldtSecondsStr+".json";
    	return fileName;
    }
    
    public Report100 report100(){
    	try {
        	InetAddress IPAddress = InetAddress.getByName(WallboxIP);
        	byte[] sendData = new byte[512];
        	byte[] receiveData = new byte[512];
        	String sentence = "report 100";
        	sendData = sentence.getBytes();
        	this.udpSocket2 = new DatagramSocket(port2);
        	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port2);
        	udpSocket2.send(sendPacket);
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        	udpSocket2.receive(receivePacket);
        	
        	String modifiedSentence = new String(receivePacket.getData());
        	report100.createFromString(modifiedSentence);
        	udpSocket2.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
    	return report100;
    }
    
    public Report100 reportI(String reportnumber){
    	Report100 reportI= new Report100();
    	try {
        	InetAddress IPAddress = InetAddress.getByName(WallboxIP);
        	byte[] sendData = new byte[512];
        	byte[] receiveData = new byte[512];
        	String sentence = "report 1" + reportnumber;
        	sendData = sentence.getBytes();
        	this.udpSocket2 = new DatagramSocket(port2);
        	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port2);
        	udpSocket2.send(sendPacket);
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        	udpSocket2.receive(receivePacket);
        	
        	String modifiedSentence = new String(receivePacket.getData());
        	
        	reportI.createFromString(modifiedSentence);
        	System.out.println("ReportI new ID= " + reportI.getSessionID());
        	//database.createLoading(reportI.getStarted(), reportI.getSessionID());
        	//database.terminateLoading(reportI.getEnded(), null, null, reportI.getEpres());
        	udpSocket2.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
    	return reportI;
    }
    
    public void report2(){
    	try {
        	InetAddress IPAddress = InetAddress.getByName(WallboxIP);
        	byte[] sendData = new byte[512];
        	byte[] receiveData = new byte[512];
        	String sentence = "report 2";
        	sendData = sentence.getBytes();
        	this.udpSocket2 = new DatagramSocket(port2);
        	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port2);
        	udpSocket2.send(sendPacket);
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        	udpSocket2.receive(receivePacket);
        	
        	String modifiedSentence = new String(receivePacket.getData());
        	report2.createFromString(modifiedSentence);
        	udpSocket2.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
    	
    }
    
    public void report3(){
    	try {
        	InetAddress IPAddress = InetAddress.getByName(WallboxIP);
        	byte[] sendData = new byte[512];
        	byte[] receiveData = new byte[512];
        	String sentence = "report 3";
        	sendData = sentence.getBytes();
        	this.udpSocket2 = new DatagramSocket(port2);
        	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port2);
        	udpSocket2.send(sendPacket);
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        	udpSocket2.receive(receivePacket);
        	
        	String modifiedSentence = new String(receivePacket.getData());
        	report3.createFromString(modifiedSentence);
        	udpSocket2.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
    	
    } 
    
    public boolean getServerState(){
    	return this.isRunning;
    }

    
    
    // ___________________________________________________________________________________
    // Observer pattern
    //____________________________________________________________________________________
    
    
}

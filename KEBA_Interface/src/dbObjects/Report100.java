package dbObjects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report100 implements Serializable{
	private static final long serialVersionUID = 2182007167123143961L;

	public Report100(){
	}
		
	private int ID;
	private int SessionID;
	private int CurrHW;
	private int Estart;
	private int Epres;
	private int startedSeconds;
	private LocalDateTime started;
	private int endedSeconds;
	private LocalDateTime ended;
	private int reason;
	private String timeQ;
	private int RFIDTag;
	private int RFIDclass;
	private int serial;
	private int sec;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public int getSessionID() {
		return SessionID;
	}
	public void setSessionID(int sessionID) {
		SessionID = sessionID;
	}
	
	public int getCurrHW() {
		return CurrHW;
	}
	public void setCurrHW(int currHW) {
		CurrHW = currHW;
	}
	
	public int getEstart() {
		return Estart;
	}
	public void setEstart(int estart) {
		Estart = estart;
	}
	
	public int getEpres() {
		return Epres;
	}
	public void setEpres(int epres) {
		Epres = epres;
	}
	
	public int getStartedSeconds() {
		return startedSeconds;
	}
	public void setStartedSeconds(int startedSeconds) {
		this.startedSeconds = startedSeconds;
	}
	
	public int getEndedSeconds() {
		return endedSeconds;
	}
	public void setEndedSeconds(int endedSeconds) {
		this.endedSeconds = endedSeconds;
	}
	
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	
	public int getRFIDTag() {
		return RFIDTag;
	}
	public void setRFIDTag(int rFIDTag) {
		RFIDTag = rFIDTag;
	}
	
	public int getRFIDclass() {
		return RFIDclass;
	}
	public void setRFIDclass(int rFIDclass) {
		RFIDclass = rFIDclass;
	}
	
	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}
	
	public int getSec() {
		return sec;
	}
	public void setSec(int sec) {
		this.sec = sec;
	}
	
	public LocalDateTime getStarted() {
		return started;
	}
	public void setStarted(LocalDateTime started) {
		this.started = started;
	}
	public void setStarted(String started) {
		if (started.length() >2){
			DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			LocalDateTime dateTime = LocalDateTime.parse(started, formatter);
			this.started = dateTime;
		}
		else {
			this.started= null;
		}
	}
	
	public LocalDateTime getEnded() {
		return ended;
	}
	public void setEnded(LocalDateTime ended) {
		this.ended = ended;
	}
	public void setEnded(String ended) {
		if (ended.length() >2){
			DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			LocalDateTime dateTime = LocalDateTime.parse(ended, formatter);
			this.ended = dateTime;
		}
		else {
			this.ended= null;
		}
	}
	
	public String getTimeQ() {
		return timeQ;
	}
	public void setTimeQ(String timeQ) {
		this.timeQ = timeQ;
	}
	

	public void createFromString(String message){
		String msg1= message.split("}")[0];
		String msg= msg1.substring(1, msg1.length()-1);
		String[] splitMessage1;
		splitMessage1= msg.split(",");
		String[] args= new String[splitMessage1.length]	;
		String[] vals= new String[splitMessage1.length]	;
		for (int i=0; i< splitMessage1.length; i++){
			String[] line= splitMessage1[i].split(":");
			args[i]= line[0];
			if (line.length>2){
				String longLine= line[1];
				for (int k= 2; k<line.length;k++){
					longLine= longLine+":"+line[k];
				}
				vals[i]= longLine;
			}
			else{
				vals[i]= line[1];
			}
			
			if (args[i].endsWith("\"")) {
				args[i]= args[i].substring(2, args[i].length()-1);
			}
			if (vals[i].endsWith("\"")) {
				vals[i]= vals[i].substring(2, vals[i].length()-1);
			}
			else {
				vals[i]= vals[i].substring(1, vals[i].length());
			}
		}
		for (int i= 0; i<args.length; i++){
			switch (args[i]){
			case "ID":
				setID(Integer.parseInt(vals[i]));
				break;
			case "Session ID":
				setSessionID(Integer.parseInt(vals[i]));
				break;
			case "Curr HW":
				setCurrHW(Integer.parseInt(vals[i]));
				break;
			case "E start":
				setEstart(Integer.parseInt(vals[i]));
				break;
			case "E pres":
				setEpres(Integer.parseInt(vals[i]));
				break;
			case "started[s]":
				setStartedSeconds(Integer.parseInt(vals[i]));
				break;
			case "ended[s]":
				setEndedSeconds(Integer.parseInt(vals[i]));
				break;
			case "reason":
				setID(Integer.parseInt(vals[i]));
				break;
			case "RFID tag":
				setRFIDTag(Integer.parseInt(vals[i]));
				break;
			case "RFID class":
				setRFIDclass(Integer.parseInt(vals[i]));
				break;
			case "Serial":
				setSerial(Integer.parseInt(vals[i]));
				break;
			case "Sec":
				setSec(Integer.parseInt(vals[i]));
				break;
			case "started":
				setStarted(vals[i]);
				break;
			case "ended":
				setEnded(vals[i]);
				break;
			case "timeQ":
				setTimeQ(vals[i]);
				break;
			default:
				break;	
			}
		}
	}
}

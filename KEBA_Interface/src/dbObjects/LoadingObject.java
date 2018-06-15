package dbObjects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LoadingObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private JSONArray loggedTimeArray;
	private JSONArray loggedEnergyArray;
	private JSONArray loggedVoltageArray;
	private JSONArray loggedCurrentArray;
	
	private int[] timeSecondsArray;
	private LocalDateTime[] timeArray;
	private int[] voltageArray;
	private int[] currentArray;
	private int[] energyArray;
	private int pluginTimeSec;
	
	private int sessionID;
	
	
	public LoadingObject(){
		
	}
	
	public LoadingObject(JSONObject loadingLog){
		this.loggedTimeArray= (JSONArray)loadingLog.get("date");
		this.loggedEnergyArray= (JSONArray)loadingLog.get("value"); 
		this.loggedVoltageArray= (JSONArray)loadingLog.get("voltage"); 
		this.loggedCurrentArray= (JSONArray)loadingLog.get("current"); 
		
		setTimeArray(loggedTimeArray);
		setVoltageArray(loggedVoltageArray);
		setEnergyArray(loggedEnergyArray);
		setCurrentArray(loggedCurrentArray);
		
		if (this.timeArray!= null){
			setTimeSecondsArray(this.timeArray);
		}
	}
	
	public void setTimeArray(JSONArray timeArray){
		if(timeArray!= null){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
			LocalDateTime dateTime; 
			this.timeArray= new LocalDateTime[timeArray.size()];
			for (int i=0; i< timeArray.size(); i++){
				try {
					dateTime= LocalDateTime.parse((String)timeArray.get(i), formatter);
				} catch (DateTimeParseException e) {
					dateTime= LocalDateTime.parse((String)timeArray.get(i), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
				}
				this.timeArray[i]= dateTime;
			}
		}
		else {
			this.timeArray= null;
		}
	}
	
	public void setTimeSecondsArray(LocalDateTime[] timeArray){			
			LocalDateTime baseLdt= timeArray[0];
			int refValSec= baseLdt.getSecond();
			int refValMin= baseLdt.getMinute()*60;
			int refValHour= baseLdt.getHour()*60*60;
			int refValDay= baseLdt.getDayOfYear()*24*60*60;
			int refSeconds= refValSec + refValMin + refValHour + refValDay;
			//System.out.println("LO: refSeconds= " + refSeconds);
			setPluginTimeSec(refSeconds);
			
			LocalDateTime actLdt;
			int actValSec;
			int actValMin;
			int actValHour;
			int actValDay;
			int actSeconds; 
			
			this.timeSecondsArray= new int[timeArray.length];
			for (int i=0; i< timeArray.length; i++){
				actLdt= timeArray[i];
				actValSec= actLdt.getSecond();
				actValMin= actLdt.getMinute()*60;
				actValHour= actLdt.getHour()*60*60;
				actValDay= actLdt.getDayOfYear()*24*60*60;
				actSeconds= actValSec + actValMin + actValHour + actValDay - refSeconds;
				this.timeSecondsArray[i]= actSeconds;
			}
	}
	
	public void setVoltageArray(JSONArray voltageArray){
		this.voltageArray= new int[voltageArray.size()];
		for (int i=0; i< voltageArray.size(); i++){
			//Long volt= new Long((int) voltageArray.get(i));
			//System.out.println()
			this.voltageArray[i]= Integer.parseInt(voltageArray.get(i).toString());
		}
	}
	
	public void setCurrentArray(JSONArray currentArray){
		this.currentArray= new int[currentArray.size()];
		for (int i=0; i< currentArray.size(); i++){
			//Long curr= new Long((int)currentArray.get(i));
			this.currentArray[i]= Integer.parseInt(currentArray.get(i).toString());
		}
	}
	
	public void setEnergyArray(JSONArray energyArray){
		this.energyArray= new int[energyArray.size()];
		for (int i=0; i< energyArray.size(); i++){
			//Long energy= new Long((int)energyArray.get(i));
			this.energyArray[i]= Integer.parseInt(energyArray.get(i).toString());
		}
	}

	public int[] getTimeSecondsArray() {
		return timeSecondsArray;
	}

	public LocalDateTime[] getTimeArray() {
		return timeArray;
	}

	public int[] getVoltageArray() {
		return voltageArray;
	}

	public int[] getCurrentArray() {
		return currentArray;
	}

	public int[] getEnergyArray() {
		return energyArray;
	}
	
	public int getChargingime(){
		return (timeSecondsArray[timeSecondsArray.length-1]-timeSecondsArray[0]);
	}

	public int getPluginTimeSec() {
		return pluginTimeSec;
	}

	public void setPluginTimeSec(int pluginTimeSec) {
		this.pluginTimeSec = pluginTimeSec;
	}

	public int getPluggedSec() {
		LocalDateTime ldt= LocalDateTime.now();
		int refValSec= ldt.getSecond();
		int refValMin= ldt.getMinute()*60;
		int refValHour= ldt.getHour()*60*60;
		int refValDay= ldt.getDayOfYear()*24*60*60;
		int refSeconds= refValSec + refValMin + refValHour + refValDay;
		return refSeconds-this.pluginTimeSec;
	}

	public String getPluggedTime() {
		int pluggedSec= getPluggedSec();
		int days= pluggedSec/(3600*24);
		pluggedSec= pluggedSec-days*3600*24;
		int hours= pluggedSec/3600;
		pluggedSec= pluggedSec-hours*3600;
		int minutes= pluggedSec/60;
		pluggedSec= pluggedSec-minutes*60;
		
		return Integer.toString(days) + " days, " +
				Integer.toString(hours) + " hours, " +
				Integer.toString(minutes) + " minutes, " + 
				Integer.toString(pluggedSec) + " seconds";
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
	
	
}

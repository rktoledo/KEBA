package serverobjects;

import java.time.LocalDateTime;

public class LoadingValue {
	private String dateTimeString;
	private String powerValue;
	private String voltageValueP1;
	private String voltageValueP2;
	private String voltageValueP3;
	private String currentValueP1;
	private String currentValueP2;
	private String currentValueP3;
	
	public LoadingValue(){
	}
	
	public LoadingValue(String dateTimeString, String powerValue, String voltageValue, String currentValue){
		this.dateTimeString= dateTimeString;
		this.powerValue= powerValue;
		this.voltageValueP1= voltageValue;
		this.currentValueP1= currentValue;
	}
	
	public LoadingValue(LocalDateTime dateTime, String powerValue, String voltageValue, String currentValue){
		this.dateTimeString= dateTime.toString();
		this.powerValue= powerValue;
		this.voltageValueP1= voltageValue;
		this.currentValueP1= currentValue;
	}
	
	public LoadingValue(String dateTimeString, String powerValue, String voltageValueP1, String voltageValueP2, String voltageValueP3,
			String currentValueP1, String currentValueP2, String currentValueP3){
		this.dateTimeString= dateTimeString;
		this.powerValue= powerValue;
		this.voltageValueP1= voltageValueP1;
		this.voltageValueP2= voltageValueP2;
		this.voltageValueP3= voltageValueP3;
		this.currentValueP1= currentValueP1;
		this.currentValueP1= currentValueP2;
		this.currentValueP1= currentValueP1;
	}
	
	public LoadingValue(LocalDateTime dateTime, String powerValue, String voltageValueP1, String voltageValueP2, String voltageValueP3,
			String currentValueP1, String currentValueP2, String currentValueP3){
		this.dateTimeString= dateTime.toString();
		this.powerValue= powerValue;
		this.voltageValueP1= voltageValueP1;
		this.voltageValueP2= voltageValueP2;
		this.voltageValueP3= voltageValueP3;
		this.currentValueP1= currentValueP1;
		this.currentValueP1= currentValueP2;
		this.currentValueP1= currentValueP1;
	}

	public String getDateTimeString() {
		return dateTimeString;
	}

	public void setDateTimeString(String dateTimeString) {
		this.dateTimeString = dateTimeString;
	}
	
	public void setDateTimeString(LocalDateTime dateTime) {
		this.dateTimeString = dateTime.toString();
	}

	public String getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(String powerValue) {
		this.powerValue = powerValue;
	}

	public String getVoltageValueP1() {
		return voltageValueP1;
	}

	public void setVoltageValueP1(String voltageValueP1) {
		this.voltageValueP1 = voltageValueP1;
	}

	public String getVoltageValueP2() {
		return voltageValueP2;
	}

	public void setVoltageValueP2(String voltageValueP2) {
		this.voltageValueP2 = voltageValueP2;
	}

	public String getVoltageValueP3() {
		return voltageValueP3;
	}

	public void setVoltageValueP3(String voltageValueP3) {
		this.voltageValueP3 = voltageValueP3;
	}

	public String getCurrentValueP1() {
		return currentValueP1;
	}

	public void setCurrentValueP1(String currentValueP1) {
		this.currentValueP1 = currentValueP1;
	}

	public String getCurrentValueP2() {
		return currentValueP2;
	}

	public void setCurrentValueP2(String currentValueP2) {
		this.currentValueP2 = currentValueP2;
	}

	public String getCurrentValueP3() {
		return currentValueP3;
	}

	public void setCurrentValueP3(String currentValueP3) {
		this.currentValueP3 = currentValueP3;
	}
}

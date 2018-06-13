package serverobjects;

public class Report2 {
	private int ID;
	private int State;
	private int Error1;
	private int Error2;
	private int Plug;
	private int Enasys;
	private int Enauser;
	private int Maxcurr;
	private int Maxcurrper;
	private int CurrHW;
	private int CurrUser;
	private int Serial;
	private int Sec;
	
	public Report2(){
	}
	
	public Report2(int ID, int State, int Plug, int MaxCurr){
		this.ID= ID;
		this.State= State;
		this.Plug= Plug;
		this.Maxcurr= MaxCurr;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public void setID(String iD) {
		ID = Integer.parseInt(iD);
	}

	public int getState() {
		return State;
	}

	public void setState(int State) {
		this.State = State;
	}
	
	public void setState(String State) {
		this.State = Integer.parseInt(State);
	}

	public int getError1() {
		return Error1;
	}

	public void setError1(int Error1) {
		this.Error1 = Error1;
	}
	
	public void setError1(String Error1) {
		this.Error1 = Integer.parseInt(Error1);
	}

	public int getError2() {
		return Error2;
	}

	public void setError2(int Error2) {
		this.Error2 = Error2;
	}
	
	public void setU3(String Error2) {
		this.Error2 = Integer.parseInt(Error2);
	}

	public int getPlug() {
		return Plug;
	}

	public void setPlug(int Plug) {
		this.Plug = Plug;
	}
	
	public void setPlug(String Plug) {
		this.Plug = Integer.parseInt(Plug);
	}

	public int getEnasys() {
		return Enasys;
	}

	public void setEnasys(int Enasys) {
		this.Enasys = Enasys;
	}
	
	public void setEnasys(String Enasys) {
		this.Enasys = Integer.parseInt(Enasys);
	}

	public int getEnauser() {
		return Enauser;
	}

	public void setEnauser(int Enauser) {
		this.Enauser = Enauser;
	}
	
	public void setEnauser(String Enauser) {
		this.Enauser = Integer.parseInt(Enauser);
	}

	public int getMaxcurr() {
		return Maxcurr;
	}

	public void setMaxcurr(int Maxcurr) {
		this.Maxcurr = Maxcurr;
	}
	
	public void setMaxcurr(String Maxcurr) {
		this.Maxcurr = Integer.parseInt(Maxcurr);
	}

	public int getMaxcurrper() {
		return Maxcurrper;
	}

	public void setMaxcurrper(int Maxcurrper) {
		this.Maxcurrper = Maxcurrper;
	}
	
	public void setMaxcurrper(String Maxcurrper) {
		this.Maxcurrper = Integer.parseInt(Maxcurrper);
	}

	public int getCurrHW() {
		return CurrHW;
	}

	public void setCurrHW(int CurrHW) {
		this.CurrHW = CurrHW;
	}
	
	public void setCurrHW(String CurrHW) {
		this.CurrHW = Integer.parseInt(CurrHW);
	}

	public int getCurrUser() {
		return CurrUser;
	}

	public void setCurrUser(int CurrUser) {
		this.CurrUser = CurrUser;
	}
	
	public void setCurrUser(String CurrUser) {
		this.CurrUser = Integer.parseInt(CurrUser);
	}

	public int getSerial() {
		return Serial;
	}

	public void setSerial(int serial) {
		this.Serial = serial;
	}
	
	public void setSerial(String serial) {
		this.Serial = Integer.parseInt(serial);
	}

	public int getSec() {
		return Sec;
	}

	public void setSec(int sec) {
		this.Sec = sec;
	}
	
	public void setSec(String sec) {
		this.Sec = Integer.parseInt(sec);
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
			case "State":
				setState(Integer.parseInt(vals[i]));
				break;
			case "Error1":
				setError1(Integer.parseInt(vals[i]));
				break;
			case "Error2":
				setError2(Integer.parseInt(vals[i]));
				break;
			case "Plug":
				setPlug(Integer.parseInt(vals[i]));
				break;
			case "Enable sys":
				setEnasys(Integer.parseInt(vals[i]));
				break;
			case "Enable user":
				setEnauser(Integer.parseInt(vals[i]));
				break;
			case "Max curr":
				setMaxcurr(Integer.parseInt(vals[i]));
				break;
			case "Max curr %":
				setMaxcurrper(Integer.parseInt(vals[i]));
				break;
			case "Serial":
				setSerial(Integer.parseInt(vals[i]));
				break;
			case "Sec":
				setSec(Integer.parseInt(vals[i]));
				break;
			default:
				break;	
			}
		}
	}
}

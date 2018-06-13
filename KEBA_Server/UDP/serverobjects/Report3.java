package serverobjects;

public class Report3 {
		
	private int ID;
	private int U1;
	private int U2;
	private int U3;
	private int I1;
	private int I2;
	private int I3;
	private int P;
	private int PF;
	private int Epres;
	private int Etotal;
	private int Serial;
	private int Sec;
	
	public Report3(){
	}
	
	public Report3(int ID, int U1, int I1, int P, int Epres){
		this.ID= ID;
		this.U1= U1;
		this.I1= I1;
		this.P= P;
		this.Epres = Epres;
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

	public int getU1() {
		return U1;
	}

	public void setU1(int u1) {
		U1 = u1;
	}
	
	public void setU1(String u1) {
		U1 = Integer.parseInt(u1);
	}

	public int getU2() {
		return U2;
	}

	public void setU2(int u2) {
		U2 = u2;
	}
	
	public void setU2(String u2) {
		U2 = Integer.parseInt(u2);
	}

	public int getU3() {
		return U3;
	}

	public void setU3(int u3) {
		U3 = u3;
	}
	
	public void setU3(String u3) {
		U3 = Integer.parseInt(u3);
	}

	public int getI1() {
		return I1;
	}

	public void setI1(int i1) {
		I1 = i1;
	}
	
	public void setI1(String i1) {
		I1 = Integer.parseInt(i1);
	}

	public int getI2() {
		return I2;
	}

	public void setI2(int i2) {
		I2 = i2;
	}
	
	public void setI2(String i2) {
		I2 = Integer.parseInt(i2);
	}

	public int getI3() {
		return I3;
	}

	public void setI3(int i3) {
		I3 = i3;
	}
	
	public void setI3(String i3) {
		I3 = Integer.parseInt(i3);
	}

	public int getP() {
		return P;
	}

	public void setP(int p) {
		P = p;
	}
	
	public void setP(String p) {
		P = Integer.parseInt(p);
	}

	public int getPF() {
		return PF;
	}

	public void setPF(int pF) {
		PF = pF;
	}
	
	public void setPF(String pF) {
		PF = Integer.parseInt(pF);
	}

	public int getEpres() {
		return Epres;
	}

	public void setEpres(int epres) {
		Epres = epres;
	}
	
	public void setEpres(String epres) {
		Epres = Integer.parseInt(epres);
	}

	public int getEtotal() {
		return Etotal;
	}

	public void setEtotal(int etotal) {
		Etotal = etotal;
	}
	
	public void setEtotal(String etotal) {
		Etotal = Integer.parseInt(etotal);
	}

	public int getSerial() {
		return Serial;
	}

	public void setSerial(int serial) {
		Serial = serial;
	}
	
	public void setSerial(String serial) {
		Serial = Integer.parseInt(serial);
	}

	public int getSec() {
		return Sec;
	}

	public void setSec(int sec) {
		Sec = sec;
	}
	
	public void setSec(String sec) {
		Sec = Integer.parseInt(sec);
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
			case "U1":
				setU1(Integer.parseInt(vals[i]));
				break;
			case "U2":
				setU2(Integer.parseInt(vals[i]));
				break;
			case "U3":
				setU3(Integer.parseInt(vals[i]));
				break;
			case "I1":
				setI1(Integer.parseInt(vals[i]));
				break;
			case "I2":
				setI2(Integer.parseInt(vals[i]));
				break;
			case "I3":
				setI3(Integer.parseInt(vals[i]));
				break;
			case "E pres":
				setEpres(Integer.parseInt(vals[i]));
				break;
			case "E total":
				setEtotal(Integer.parseInt(vals[i]));
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

package dbMySQL;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import keba.rmiinterface.KEBADBInterface;

import serverobjects.LatestSessionID;
import dbDAOInterface.DbDAO;
import dbObjects.LoadingData;
import dbStrings.StringsForDB;
import static java.lang.Math.toIntExact;

@SuppressWarnings("static-access")
public class MySqlDb implements DbDAO, KEBADBInterface{
	
	String test= "";
    
    private static Statement s; 
    
    private static StringsForDB sdb;
    
    private Connection conn = null;
    
    private int currentLoadingID;
    private int latestSessionID;
    
    @Override
    public void setLatestSessionID(LatestSessionID latestSessionID){
    	this.latestSessionID= latestSessionID.getLatestSessionID();
    }
    
    @Override
    public void setCurrentlLoadingID(int loadingID){
    	this.currentLoadingID= loadingID;
    }
    
    @Override
    public int getCurrentLoadingID(){
    	return this.currentLoadingID;
    }
    
    @Override
    public int getCurrentLoadingID(int sessionID){
    	this.currentLoadingID= 0;
    	String query= "SELECT " + sdb.loadID + 
    			" FROM tbloadings WHERE " + sdb.sessionID + "= "
    			+ sessionID;
    	ResultSet rs;
    	Statement stmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			stmt = conn.createStatement();
			//System.out.println("Query= " + query);
			
			 rs = stmt.executeQuery(query);
			 if (rs.next()){
				 this.currentLoadingID= rs.getInt(sdb.loadID);
			 }
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return this.currentLoadingID;
    }
    
    /** The String for Database creation. */
    private static final String createDB = "CREATE DATABASE IF NOT EXISTS " + sdb.keba;
    
    /** The Strings for Table Logins creation. */
    // Logins-Table
    private static final String createTableLoadings = "CREATE TABLE IF NOT EXISTS " + sdb.tableLoadings + " ("
			+ sdb.loadID + " INT NOT NULL AUTO_INCREMENT, "
			+ sdb.sessionID + " INT DEFAULT 0, "
			+ sdb.loadStartDate + " TIMESTAMP DEFAULT now(), "
			+ sdb.loadEnddate + " TIMESTAMP DEFAULT now(), "
			+ sdb.loadTime + " INT DEFAULT NULL, "
			+ sdb.plugTime + " INT DEFAULT NULL, "
			+ sdb.loadedEnergy + " INT DEFAULT NULL, "
			+ sdb.loadFilePath + " VARCHAR(100) DEFAULT NULL, "
			+ sdb.statefilePath + " VARCHAR(100) DEFAULT NULL, "
			+ sdb.loadingComplete + " BOOLEAN DEFAULT TRUE, "
			+ sdb.loadingUsable + " BOOLEAN DEFAULT TRUE, "
			+ "PRIMARY KEY (" + sdb.loadID + "));";
	
       
    public MySqlDb(){
    	//System.out.println("In constructor of MySqlDb");
    	//Class.forName("com.mysql.cj.jdbc.Driver");
		//conn = DriverManager.getConnection(DBURL, USER, PASS);
		currentLoadingID= checkForActualLoading();
        
    	boolean res= createDB();
    	//System.out.println("DB res= " + res);
        if (res){
     	   //System.out.println("c1 " + createTable(createTableLoadings, sdb.tableLoadings));
        }
    }
    
    private int checkForActualLoading(){
    	int actualLoading= 0;
    	String query= "SELECT * FROM tbloadings WHERE " + sdb.loadingComplete + "= FALSE";
    	ResultSet rs;
    	PreparedStatement preparedStmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Connection= " + conn.toString());
			//System.out.println("Statement= " + preparedStmt.toString());
			
			 rs = preparedStmt.executeQuery();
			 if (rs.next()){
				 rs.last();
				 if (rs.getInt(sdb.sessionID)== latestSessionID){
					 actualLoading= latestSessionID;
				 }
			 }
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return actualLoading;
    }

	public boolean createDB(){
		boolean res= false;
		
        try {
        	conn = MySqlDAOFactory.createConnection();
        	s= conn.createStatement();
        	int querryresult= s.executeUpdate(createDB);
            //System.out.println("t1" + querryresult);
            //System.out.println("t2" + s.executeUpdate("show databases like 'keba'"));
            res= true;
            conn.close();
        } catch (SQLException e) {
        	res= false;
            //System.out.println("e1 " + e);
        }		
        return res;
	}
	
	/** Returns:
	 *  false when table exists already,
	 *  true when table was created new. 
	 */
    public boolean createTable(String command, String tablename){
    	boolean res;
    	//System.out.println("MYSQLCREATEDB: command= " + command);
    	//System.out.println("MYSQLCREATEDB: tableName= " + tablename);
        PreparedStatement preparedStatement = null;
        try {
            conn = MySqlDAOFactory.createConnection();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tablename, null);
            if (tables.next()){
            	// Table exists. No action required.
            	res= false;
            }
            else {
            	preparedStatement = conn.prepareStatement(command);
            	//System.out.println("MYSQLCREATEDB: preparedStatement= " + preparedStatement);
            	preparedStatement.execute();
                res= true;
            }
            conn.close();
        } catch (SQLException e) {
        	res= false;
            //System.out.println("e3 " + e.getMessage());
        } finally {
            /*try {
                conn.close();
            } catch (Exception cse) {
            	//System.out.println("e5 " + cse.getMessage());
            }*/
        }
        return res;
    }

	@Override
	public int createLoading(LocalDateTime ldtStart) {
		//System.out.println("createLoading");
		String query = " insert into " 
				+ sdb.tableLoadings + " (" 
				+ sdb.loadStartDate + ") "
				//+ sdb.loadEnddate + ", "
				//+ sdb.loadTime + ", "
				//+ sdb.loadFilePath + ", "
				//+ sdb.statefilePath + ")"
				//+ " values (?, ?, ?, ?, ?)";
				+ " values (?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			java.sql.Timestamp sqlDate = java.sql.Timestamp.valueOf(ldtStart);
			preparedStmt.setTimestamp(1, sqlDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
			
			//System.out.println("Statement= " + preparedStmt.toString());

			// execute the preparedstatement
			//System.out.println("Statement= " + preparedStmt.toString());
			preparedStmt.execute();
			ResultSet rs = preparedStmt.getGeneratedKeys();
			rs.next();
			this.currentLoadingID= rs.getInt(1);
			//System.out.println("ID of entry= " + currentLoadingID);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.currentLoadingID;
	}
	
	@Override
	public int createLoading(LocalDateTime ldtStart, int sessionID) {
		//System.out.println("createLoading");
		String query = " insert into " 
				+ sdb.tableLoadings + " (" 
				+ sdb.sessionID + ", "
				+ sdb.loadStartDate + ") "
				//+ sdb.loadEnddate + ", "
				//+ sdb.loadTime + ", "
				//+ sdb.loadFilePath + ", "
				//+ sdb.statefilePath + ")"
				//+ " values (?, ?, ?, ?, ?)";
				+ " values (?, ?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			java.sql.Timestamp sqlDate = java.sql.Timestamp.valueOf(ldtStart);
			preparedStmt.setInt(1, sessionID);
			preparedStmt.setTimestamp(2, sqlDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
			
			//System.out.println("Statement= " + preparedStmt.toString());

			// execute the preparedstatement
			//System.out.println("Statement= " + preparedStmt.toString());
			preparedStmt.execute();
			ResultSet rs = preparedStmt.getGeneratedKeys();
			rs.next();
			this.currentLoadingID= rs.getInt(1);
			//System.out.println("ID of entry= " + currentLoadingID);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.currentLoadingID;
	}

	@Override
	public LocalDateTime getLoadingStart(int loadID) {
		String query = " SELECT " + sdb.loadStartDate + 
				" FROM " + sdb.tableLoadings + 
				" WHERE " + sdb.loadID + 
				"= " + loadID;
		
		LocalDateTime dateTime= null;
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			ResultSet rs = preparedStmt.executeQuery();
			
			if (rs.next() == false) { 
				//System.out.println("ResultSet in empty in Java"); 
			} else { 
				do { 
					java.sql.Timestamp sqlDateTime= rs.getTimestamp(sdb.loadStartDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
					if (sqlDateTime != null){
						dateTime= sqlDateTime.toLocalDateTime();
					}
				} 
				while (rs.next()); 
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTime;
	}

	@Override
	public boolean addLoadingEnd(LocalDateTime ldtEnd) {
		try {
			conn = MySqlDAOFactory.createConnection();
			// TODO Auto-generated method stub
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public LocalDateTime getLoadingEnd(int item) {
		conn = MySqlDAOFactory.createConnection();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoadTime(int item) {
		try {
			conn = MySqlDAOFactory.createConnection();
			// TODO Auto-generated method stub
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int addLoadTime(int loadID, LocalDateTime ldtEnd) {
		try {
			conn = MySqlDAOFactory.createConnection();
			// TODO Auto-generated method stub
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getLoadedEnergy(int item) {
		try {
			conn = MySqlDAOFactory.createConnection();
			// TODO Auto-generated method stub
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int addLoadedEnergy(int loadedEnergy) {
		try {
			conn = MySqlDAOFactory.createConnection();
			// TODO Auto-generated method stub
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void addLoadingFilePath(String loadingFilePath) {
		String query = " UPDATE " + sdb.tableLoadings + 
				" set " + sdb.loadFilePath + "= '" + loadingFilePath +
				"' WHERE " + sdb.loadID + 
				"= " + currentLoadingID;
		
		System.out.println(query);
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			preparedStmt.execute();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getLoadingFilePath(int loadID) {
		String query = " SELECT " + sdb.loadFilePath + 
				" FROM " + sdb.tableLoadings + 
				" WHERE " + sdb.loadID + 
				"= " + loadID;
		
		String filePath= null;
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			ResultSet rs = preparedStmt.executeQuery();
			
			if (rs.next() == false) { 
				//System.out.println("ResultSet in empty in Java"); 
			} else { 
				do { 
					filePath= rs.getString(sdb.loadFilePath);
				} 
				while (rs.next()); 
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	
	@Override
	public String getLoadingFilePathbySessionID(int sessionID) {
		String query = " SELECT " + sdb.loadFilePath + 
				" FROM " + sdb.tableLoadings + 
				" WHERE " + sdb.sessionID + 
				"= " + sessionID;
		
		String filePath= null;
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			ResultSet rs = preparedStmt.executeQuery();
			
			if (rs.next() == false) { 
				//System.out.println("ResultSet in empty in Java"); 
			} else { 
				do { 
					filePath= rs.getString(sdb.loadFilePath);
				} 
				while (rs.next()); 
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	@Override
	public void addStateFilePath(String stateFilePath) {
		String query = " UPDATE " + sdb.tableLoadings + 
				" set " + sdb.statefilePath + "= '" + stateFilePath +
				"' WHERE " + sdb.loadID + 
				"= " + currentLoadingID;
		
		System.out.println(query);
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			preparedStmt.execute();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getStateFilePath(int loadID) {
		String query = " SELECT " + sdb.statefilePath + 
				" FROM " + sdb.tableLoadings + 
				" WHERE " + sdb.loadID + 
				"= " + loadID;
		
		String filePath= null;
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			ResultSet rs = preparedStmt.executeQuery();
			
			if (rs.next() == false) { 
				//System.out.println("ResultSet in empty in Java"); 
			} else { 
				do { 
					filePath= rs.getString(sdb.statefilePath);
				} 
				while (rs.next()); 
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	
	@Override
	public String getStateFilePathbySessionID(int sessionID) {
		String query = " SELECT " + sdb.statefilePath + 
				" FROM " + sdb.tableLoadings + 
				" WHERE " + sdb.sessionID + 
				"= " + sessionID;
		
		String filePath= null;
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			ResultSet rs = preparedStmt.executeQuery();
			
			if (rs.next() == false) { 
				//System.out.println("ResultSet in empty in Java"); 
			} else { 
				do { 
					filePath= rs.getString(sdb.statefilePath);
				} 
				while (rs.next()); 
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	@Override
	public boolean terminateLoading(LocalDateTime loadingEndTime, String loadFilePath, String stateFilePath, int loadedEnergy) {
		//System.out.println("terminateLoading");
		String query = " update " 
				+ sdb.tableLoadings + " set " 
				+ sdb.loadEnddate + "=? , "
				+ sdb.loadTime + "=? , "
				+ sdb.loadFilePath + "=? , "
				+ sdb.loadingComplete + "=? , "
				+ sdb.loadedEnergy + "=? , "
				+ sdb.statefilePath + "=? "
				+ " WHERE " +  sdb.loadID + "= ?";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			java.sql.Timestamp sqlDate = java.sql.Timestamp.valueOf(loadingEndTime);
			preparedStmt.setTimestamp(1, sqlDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
			preparedStmt.setInt(2, calculateLoadTime(currentLoadingID, loadingEndTime));
			preparedStmt.setString(3, loadFilePath);
			preparedStmt.setBoolean(4, true);
			preparedStmt.setInt(5, loadedEnergy);
			preparedStmt.setString(6, stateFilePath);
			preparedStmt.setInt(7, currentLoadingID);
		

			// execute the preparedstatement
			//System.out.println("Statement= " + preparedStmt.toString());
			preparedStmt.execute();
			//System.out.println("ID of entry= " + currentLoadingID);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public int calculateDifference(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		long difference= Duration.between(startDateTime, endDateTime).toMillis()/1000;
		return toIntExact(difference);
	}

	@Override
	public int calculateLoadTime(int loadID, LocalDateTime endDateTime) {
		LocalDateTime startDateTime= getLoadingStart(loadID);
		return calculateDifference(startDateTime, endDateTime);
	}

	@Override
	public boolean checkIfSessionIDExists(int sessionID) {
		String query = " SELECT * FROM " + 
				sdb.tableLoadings + 
				" WHERE " + sdb.sessionID + 
				"= " + sessionID;
		
		System.out.println(query);
		
		Boolean exists= false;
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			ResultSet rs = preparedStmt.executeQuery();
			
			if (rs.next() == false) { 
				exists= false;
			} else { 
				exists= true;
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
	}

	@Override
	public void setSessionID(int loadID, int sessionID) {
		String query = " UPDATE " + sdb.tableLoadings + 
				" set " + sdb.sessionID + "= " + sessionID +
				" WHERE " + sdb.loadID + 
				"= " + loadID;
		
		try {
			conn = MySqlDAOFactory.createConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//System.out.println("Statement= " + preparedStmt.toString());
			preparedStmt.execute();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getSessionID(int loadID) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
	/* 
	 * Remote Methods that can be used from remote clients
	 * @see keba.rmiinterface.KEBADBInterface
	 */
	

	@Override
	public LoadingData getActualLoading() throws RemoteException {
		LoadingData data= new LoadingData();
    	
		//String query= "SELECT * FROM tbloadings where " + sdb.sessionID + "= 138 ORDER BY " + sdb.sessionID + " DESC LIMIT 1";
		String query= "SELECT * FROM tbloadings ORDER BY " + sdb.sessionID + " DESC LIMIT 1";
		
		//SELECT * FROM Table ORDER BY ID DESC LIMIT 1
		
    	ResultSet rs;
    	Statement stmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			stmt = conn.createStatement();
			//System.out.println("Query= " + query);
			
			 rs = stmt.executeQuery(query);
			 if (rs.next()){
				 data.setLoadID(rs.getInt(sdb.loadID));
				 data.setSessionID(rs.getInt(sdb.sessionID));
				 data.setLoadTime(rs.getInt(sdb.loadTime));
				 data.setLoadedEnergy(rs.getInt(sdb.loadedEnergy));
				 data.setComplete(rs.getBoolean(sdb.loadingComplete));
				 data.setUsable(rs.getBoolean(sdb.loadingUsable));
				 
				 	java.sql.Timestamp sqlDateTime= rs.getTimestamp(sdb.loadStartDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
				 	if (sqlDateTime != null){
				 		data.setStartDateTime(sqlDateTime.toLocalDateTime());
					}
				 	
				 	sqlDateTime= rs.getTimestamp(sdb.loadEnddate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
				 	if (sqlDateTime != null){
				 		data.setEndDateTime(sqlDateTime.toLocalDateTime());
					}
				 
				 if (rs.getString(sdb.loadFilePath) != null) {
					 data.setLoadFileAvailable(true);
					 data.setLoadFilePath(rs.getString(sdb.loadFilePath));
				 }
				 
				 if (rs.getString(sdb.statefilePath) != null) {
					 data.setStateFileAvailable(true);
					 data.setStateFilePath(rs.getString(sdb.statefilePath));
				 }
			 }
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("DB: Return LoadingData for SessionID: " + data.getSessionID());
    	return data;
	}

	@Override
	public LoadingData getLoadingObject(int sessionID) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<LoadingData> getLoadingsFrom(int loadID)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<LoadingData> getLoadingsTo(int loadID)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<LoadingData> getLoadingsFromTo(int loadIDstart,
			int loadIDend) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<LoadingData> getAllLoadings()  throws RemoteException{
		//System.out.println("DB: GetAllLoadings called!");
		
		ArrayList<LoadingData> datas= new ArrayList<LoadingData>();
		LoadingData data;
    	
		String query= "SELECT * FROM tbloadings";
		
    	ResultSet rs;
    	Statement stmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			stmt = conn.createStatement();
			//System.out.println("Statement= " + stmt.toString());
			
			 rs = stmt.executeQuery(query);
			 while (rs.next()){
				 data= new LoadingData();
				 data.setLoadID(rs.getInt(sdb.loadID));
				 data.setSessionID(rs.getInt(sdb.sessionID));
				 data.setLoadTime(rs.getInt(sdb.loadTime));
				 data.setLoadedEnergy(rs.getInt(sdb.loadedEnergy));
				 data.setComplete(rs.getBoolean(sdb.loadingComplete));
				 data.setUsable(rs.getBoolean(sdb.loadingUsable));
				 
				 	java.sql.Timestamp sqlDateTime= rs.getTimestamp(sdb.loadStartDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
				 	if (sqlDateTime != null){
				 		data.setStartDateTime(sqlDateTime.toLocalDateTime());
					}
				 	
				 	sqlDateTime= rs.getTimestamp(sdb.loadEnddate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
				 	if (sqlDateTime != null){
				 		data.setEndDateTime(sqlDateTime.toLocalDateTime());
					}
				 
				 if (rs.getString(sdb.loadFilePath) != null) {
					 data.setLoadFileAvailable(true);
					 data.setLoadFilePath(rs.getString(sdb.loadFilePath));
				 }
				 
				 if (rs.getString(sdb.statefilePath) != null) {
					 data.setStateFileAvailable(true);
					 data.setStateFilePath(rs.getString(sdb.statefilePath));
				 }
				 datas.add(data);
			 }
			 rs.last();
			 //System.out.println("DB: GetAllLoadings rs size= " + rs.getRow());
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("DB: GetAllLoadings size of return Array= " + datas.size());
		return datas;
	}

	@Override
	public LoadingData getLoading(int loadID) throws RemoteException{
		LoadingData data= new LoadingData();
    	
		String query= "SELECT * FROM tbloadings WHERE " + sdb.loadID + "= "
    			+ loadID;
		
    	ResultSet rs;
    	Statement stmt;
		try {
			conn = MySqlDAOFactory.createConnection();
			stmt = conn.createStatement();
			//System.out.println("Statement= " + stmt.toString());
			
			 rs = stmt.executeQuery(query);
			 if (rs.next()){
				 data.setLoadID(rs.getInt(sdb.loadID));
				 data.setSessionID(rs.getInt(sdb.sessionID));
				 data.setLoadTime(rs.getInt(sdb.loadTime));
				 data.setLoadedEnergy(rs.getInt(sdb.loadedEnergy));
				 data.setComplete(rs.getBoolean(sdb.loadingComplete));
				 data.setUsable(rs.getBoolean(sdb.loadingUsable));
				 
				 	java.sql.Timestamp sqlDateTime= rs.getTimestamp(sdb.loadStartDate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
				 	if (sqlDateTime != null){
				 		data.setStartDateTime(sqlDateTime.toLocalDateTime());
					}
				 	
				 	sqlDateTime= rs.getTimestamp(sdb.loadEnddate, Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich")));
				 	if (sqlDateTime != null){
				 		data.setEndDateTime(sqlDateTime.toLocalDateTime());
					}
				 
				 if (rs.getString(sdb.loadFilePath) != null) {
					 data.setLoadFileAvailable(true);
					 data.setLoadFilePath(rs.getString(sdb.loadFilePath));
				 }
				 
				 if (rs.getString(sdb.statefilePath) != null) {
					 data.setStateFileAvailable(true);
					 data.setStateFilePath(rs.getString(sdb.statefilePath));
				 }
			 }
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return data;
	}
    
}

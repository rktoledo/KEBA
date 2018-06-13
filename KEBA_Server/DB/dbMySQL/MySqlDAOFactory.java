package dbMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import db.DbDAOFactory;
import dbDAOInterface.DbDAO;

public class MySqlDAOFactory extends DbDAOFactory{
 
    /** The driver-class. */
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    /** The url to dbmysql. KEBA WALLBOX*/
    //public static final String DBURLKEBA = "jdbc:mysql://localhost:3306/kebawallbox";
    //public static final String DBURLKEBA = "jdbc:mysql://localhost:3306/kebawallbox?useUnicode=true&" +
    //		"useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String DBURLKEBA = "jdbc:mysql://localhost:3306/keba?useUnicode=true&" +
    		"useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    /** The username for dbmysql-operations. */
    public static final String USER = "keba";
    /** The password for dbmysql-operations. */
    public static final String PASS = "wallbox";
    
    private static Connection conn;
   
    public static Connection createConnection() {
        conn = null;
        try {
            Class.forName(DRIVER);
          
            conn = DriverManager.getConnection(DBURLKEBA, USER, PASS);
            //System.out.println("MysqlDAOFact: conn= " + conn.toString()	);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
        	System.out.println(e);
        }
        return conn;
    }
 
	@Override
	public DbDAO getDbDAO() {
		return new MySqlDb();
	}

}

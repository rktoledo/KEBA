package db;

import dbDAOInterface.DbDAO;
import dbMySQL.MySqlDAOFactory;

/**
 * Interface für die Datenbank "KEBA WALLBOX"
 * @author Ron Peyer
 * @version 1.0
 * @since March 2018
 *
 */
public abstract class DbDAOFactory {
	
    /** Abstract method for the CreateDbDAO. */
    public abstract DbDAO getDbDAO(); 
    /** Abstract method for the DataDAO. */
    
    public static DbDAOFactory getDbDAOFactory (String type){ 
        if (type.equalsIgnoreCase("mysql")){
            return new MySqlDAOFactory();
        }else{
            return null;
        }
    }
}

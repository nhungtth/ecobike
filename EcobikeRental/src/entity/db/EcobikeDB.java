package entity.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import utils.Utils;

public class EcobikeDB {
	private static Logger LOGGER = Utils.getLogger(Connection.class.getName());

    public static Connection getConnection() {
    	Connection connect = null;
        try {        	
        	Class.forName("com.mysql.jdbc.Driver"); 
        	String url = "jdbc:mysql://localhost:3306/ecobike";
        	connect = DriverManager.getConnection(url, "test123", "test123");  
            LOGGER.info("Connect database successfully");
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        } 
        return connect;
    }
}

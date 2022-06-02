package BD_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccessBaseDonnee {
	
	private static Connection connection = null;
	
	private AccessBaseDonnee() throws SQLException {
	  connection = DriverManager.getConnection("jdbc:mysql://localhost/soa_db?severTimezone=UTC","root","");
	}
	
	public static Connection getInstance() throws SQLException{
		if(connection==null) {
			new AccessBaseDonnee(); 
		}
		return connection;
	}

}

package org.dementhium.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.mysql.jdbc.Driver;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class DatabaseManager {

	private static Driver driver;

	public static DatabaseManager create(String host, String database, String username, String password) throws SQLException {
		if (driver == null) {
			driver = new Driver();
		}
		return new DatabaseManager(host, database, username, password);
	}

	private String host;
	private String database;
	private String username;
	private String password;

	private Connection connection;
	private Statement statement;

	private DatabaseManager(String host, String database, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void establishConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?jdbcCompliantTruncation=false", username, password);
			statement = connection.createStatement();
		} catch (Exception e) {
			//if (Misc.isWindows()) { //So it's only for Emperor.
			//	RS2ServerBootstrap.sqlDisabled = true;
			//}
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String query) throws SQLException{
		ResultSet results = statement.executeQuery(query);	
		return results;
	}
	
	public void executeUpdate(String query) throws SQLException {
		statement.executeUpdate(query);
	}

}

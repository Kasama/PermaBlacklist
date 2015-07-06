package com.kasama.permablacklist;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {

	Connection conn;
	Statement statement;

	public DataManager(String database) {
		File file = new File(database);
		boolean dbExists = file.exists();
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			statement = conn.createStatement();
			String sql;
			if (!dbExists) {
				sql = "CREATE TABLE blacklist" +
					" (" +
					"NAME TEXT NOT NULL, " +
					"CPFCNPJ TEXT NOT NULL" +
					" )";
				statement.execute(sql);
			}
//			sql = "INSERT INTO BLACKLISTEDS (ID, NAME, CPFCNPJ)" +
//				"VALUES (1, 'Jose', '12786421');";
//			statement.execute(sql);
		} catch (SQLException e) {
			System.err.println("An exception was thrown while trying to open the database connection!");
		} catch (ClassNotFoundException e) {
			System.err.println("Fatal error: Could not find class JDBC!");
		} finally {
			cleanUp();
		}
	}

	public void cleanUp(){
		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("An exception was thrown while trying to close the connection to the database");
		}
	}

	@Override
	public void finalize() throws Throwable {
		cleanUp();
		super.finalize();
	}
}

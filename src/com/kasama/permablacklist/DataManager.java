package com.kasama.permablacklist;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataManager {

	public DataManager(String database) {
		File file = new File(database);
		boolean dbExists = file.exists();
		Connection c;
		Statement statement;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			statement = c.createStatement();
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
			statement.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

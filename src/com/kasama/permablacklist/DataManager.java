package com.kasama.permablacklist;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

	private static final String TABLE = "blacklist";
	private static final String NAME = "NAME";
	private static final String CPFCNPJ = "CPFCNPJ";
	Connection conn;

	public DataManager(String database) {
		File file = new File(database);
		System.out.println("table exists at '"+file.getAbsolutePath()+"'!");
		boolean dbExists = file.exists();
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			Statement statement = conn.createStatement();
			String sql;
			if (!dbExists) {
				sql = "CREATE TABLE "+TABLE+
					" (" +
					NAME + " TEXT NOT NULL, " +
					CPFCNPJ + " TEXT NOT NULL" +
					" )";
				statement.execute(sql);
				System.out.println("created table at '"+file.getAbsolutePath()+"'!");
			}
			statement.close();
//			sql = "INSERT INTO BLACKLISTEDS (ID, NAME, CPFCNPJ)" +
//				"VALUES (1, 'Jose', '12786421');";
//			statement.execute(sql);
		} catch (SQLException e) {
			System.err.println("An exception was thrown while trying to open the database connection!");
			cleanUp();
		} catch (ClassNotFoundException e) {
			System.err.println("Fatal error: Could not find class JDBC!");
		}
	}

	public void cleanUp(){
		try {
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

	public List<BlacklistEntry> requestAllEntries() {
		List<BlacklistEntry> ret = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLE+";");
			while (rs.next()){
				ret.add(new BlacklistEntry(rs.getString(NAME), rs.getString(CPFCNPJ)));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return ret;
	}

	public boolean addEntry(BlacklistEntry entry){

		try {
			Statement statement = conn.createStatement();
			String sql = "INSERT INTO "+TABLE+" ("+NAME+","+CPFCNPJ+")" +
				"VALUES ('"+entry.getName()+"','"+entry.getCpfcnpj()+"');";
			statement.execute(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}
}

package com.kasama.permablacklist;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

	private static final String TABLE = "blacklist";
	private static final String NAME = "NAME";
	private static final String CPFCNPJ = "CPFCNPJ";

	File database = null;

	public DataManager(String database) {
		File file = new File(database);
		this.database = file;
		Connection conn = connect(file);
		close(conn);
	}

	public Connection connect(File file) {
		boolean dbExists = file.exists();
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(
				"jdbc:sqlite:" + file.getAbsolutePath()
			);
			Statement statement = conn.createStatement();
			String sql;
			if (!dbExists) {
				sql = "CREATE TABLE " + TABLE +
					" (" +
					NAME + " TEXT NOT NULL, " +
					CPFCNPJ + " TEXT NOT NULL" +
					" )";
				statement.execute(sql);
			}
			statement.close();
		} catch (SQLException e) {
			System.err.println("An exception was thrown while trying to open the database connection!");
			close(conn);
		} catch (ClassNotFoundException e) {
			System.err.println("Fatal error: Could not find class JDBC!");
		}

		return conn;
	}

	private void close(Connection conn){
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			System.err.println(
				"An exception was thrown while trying to close the connection to the database"
			);
		}
	}

	public List<BlacklistEntry> requestAllEntries() {
		List<BlacklistEntry> ret = new ArrayList<>();

		Connection conn = null;
		try {
			conn = connect(database);
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLE+";");
			while (rs.next()){
				ret.add(new BlacklistEntry(rs.getString(NAME), rs.getString(CPFCNPJ)));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(conn);
		}


		return ret;
	}

	public boolean addEntry(BlacklistEntry entry){

		Connection conn = null;
		try {
			conn = connect(database);
			Statement statement = conn.createStatement();
			String sql = "INSERT INTO "+TABLE+" ("+NAME+","+CPFCNPJ+")" +
				"VALUES ('"+entry.getName()+"','"+entry.getCpfcnpj()+"');";
			statement.execute(sql);
			statement.close();
			close(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}

		return true;
	}
}

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
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(
				"jdbc:sqlite:" + file.getAbsolutePath()
			);
			statement = conn.createStatement();
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
			System.err.println(
				"An exception was thrown while trying to open the database connection!"
			);
			close(conn);
			conn = null;
		} catch (ClassNotFoundException e) {
			System.err.println("Fatal error: Could not find class JDBC!");
		} finally {
			close(statement);
		}

		return conn;
	}

	private void close(AutoCloseable... closeables){
		try {
			int i = 0;
			for (AutoCloseable c : closeables){
				if (c != null)
					c.close();
			}
		} catch (Exception e) {
			System.err.println(
				"An exception was thrown while trying to close the database"
			);
		}
	}

	public List<BlacklistEntry> requestAllEntries() {
		List<BlacklistEntry> ret = new ArrayList<>();

		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			conn = connect(database);
			statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * FROM "+TABLE+";");
			while (rs.next()){
				ret.add(new BlacklistEntry(rs.getString(NAME), rs.getString(CPFCNPJ)));
			}
		} catch (SQLException e) {
			System.err.println("Could not query database!");
		}finally {
			close(rs, statement, conn);
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
		} catch (SQLException e) {
			System.err.println("Not possible to write to the database!");
		} finally {
			close(conn);
		}

		return true;
	}
}

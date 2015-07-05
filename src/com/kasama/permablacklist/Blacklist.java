package com.kasama.permablacklist;

public class Blacklist {
	private static Blacklist ourInstance = null;
	private DataManager dataManager = null;
	private SessionManager sessionManager = null;

	public static Blacklist getInstance(String direcotry) {
		if (ourInstance == null) {
			ourInstance = new Blacklist(direcotry);
		}
		return ourInstance;
	}

	private Blacklist(String directory) {
		dataManager = new DataManager(directory);
		sessionManager = new SessionManager();
	}

}

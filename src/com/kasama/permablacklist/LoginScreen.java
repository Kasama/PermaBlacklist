package com.kasama.permablacklist;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginScreen extends Scene {

	public LoginScreen(Parent root) {
		super(root);
	}

	public static Scene getScene() {

		Parent root;
		LoginScreen login = null;

		FXMLLoader loader = new FXMLLoader(
			LoginScreen.class.getClassLoader().getResource("loginScreen.fxml")
		);

		try {
			login = new LoginScreen(loader.load());
			loader.setController(login);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load fxml file!");
		}

		return login;

	}


}

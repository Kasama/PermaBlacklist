package com.kasama.permablacklist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root;
		FXMLLoader loader = new FXMLLoader(
			MainScreen.class.getClassLoader().getResource(
				"com/kasama/permablacklist/mainScreen.fxml"
			)
		);
		root = loader.load();
		primaryStage.setTitle("Blacklist Permanente");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}

package com.kasama.permablacklist;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setScene(MainScreen.getScene());
		primaryStage.show();

	}

	public static void main(String[] args) {
        launch(args);
	}

}

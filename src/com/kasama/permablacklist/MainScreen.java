package com.kasama.permablacklist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MainScreen extends Scene {

	@FXML
	private TextField filterText;
	@FXML
	private TableView blacklistTable;
	@FXML
	private TableColumn<BlacklistEntry, String> blacklistCPFCol;
	@FXML
	private TableColumn<BlacklistEntry, String> blacklistNameCol;

	private ObservableList<BlacklistEntry> blacklisteds;

	public MainScreen(Parent root) {
		super(root);
		initializeComponents();
	}

	public static Scene getScene() {

		Parent root;
		MainScreen screen = null;

		FXMLLoader loader = new FXMLLoader(
			MainScreen.class.getClassLoader().getResource("mainScreen.fxml")
		);

		try {
			screen = new MainScreen(loader.load());
			loader.setController(screen);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load fxml file!");
		}

		return screen;

	}

	public void initializeComponents() {

		blacklisteds = FXCollections.observableArrayList();

		refreshTable();

		FilteredList<BlacklistEntry> blacklistFilteredList;
		blacklistFilteredList = new FilteredList<>(blacklisteds, u -> true);

		filterText.textProperty().addListener(
			(observable, oldValue, newValue) -> {
				blacklistFilteredList.setPredicate(
					entry -> {
						if (newValue == null || newValue.isEmpty()) return true;
						String lowerCaseValue = newValue.toLowerCase();
						return
							entry.getName().toLowerCase().contains(
								lowerCaseValue
							) ||
							entry.getCpfcnpj().toLowerCase().contains(
								lowerCaseValue
							) ||
							DocumentValidator.numberOnlyCPFCNPJ(
								entry.getCpfcnpj()
							).toLowerCase().contains(
								lowerCaseValue
							);
					}
				);
			}
		);

		SortedList<BlacklistEntry> sortedBlacklist;
		sortedBlacklist = new SortedList<>(blacklistFilteredList);
		sortedBlacklist.comparatorProperty().bind(blacklistTable.comparatorProperty());

		blacklistTable.setItems(sortedBlacklist);

		tableUserID.setCellValueFactory(
			cellData -> cellData.getValue().IDProperty()
		);
		tableUserName.setCellValueFactory(
			cellData -> cellData.getValue().nameProperty()
		);

	}

	private void refreshTable() {
	}

}

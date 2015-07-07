package com.kasama.permablacklist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class MainScreen extends Scene {

	@FXML
	private TextField filterText;
	@FXML
	private TableView<BlacklistEntry> blacklistTable;
	@FXML
	private TableColumn<BlacklistEntry, String> blacklistCPFCol;
	@FXML
	private TableColumn<BlacklistEntry, String> blacklistNameCol;

	private DataManager dataManager;
	private ObservableList<BlacklistEntry> blacklisted;

	public MainScreen(Parent root) {
		super(root);
		initializeComponents();
		dataManager = new DataManager("db/database.db");
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

		blacklisted = FXCollections.observableArrayList();

		refreshTable();

		FilteredList<BlacklistEntry> blacklistFilteredList;
		blacklistFilteredList = new FilteredList<>(blacklisted, u -> true);

		filterText.textProperty().addListener(
			(observable, oldValue, newValue) -> {
				blacklistFilteredList.setPredicate(
					entry -> {
						if (newValue == null || newValue.isEmpty()) return true;
						String lowerCaseValue = newValue.toLowerCase();
						return entry.getName().toLowerCase().contains(
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
		sortedBlacklist.comparatorProperty().bind(
			blacklistTable.comparatorProperty()
		);

		blacklistTable.setItems(sortedBlacklist);

		blacklistNameCol.setCellValueFactory(
			cellData -> cellData.getValue().nameProperty()
		);
		blacklistCPFCol.setCellValueFactory(
			cellData -> cellData.getValue().cpfcnpjProperty()
		);

	}

	private void addEntry() {

		NewEntryDialog newEntryDialog = new NewEntryDialog();
		Optional<BlacklistEntry> entry = newEntryDialog.showAndWait();

		if (entry.isPresent()) {
			if (entry.get().getName().equals("")) {
				Alert alert = new Alert(
					Alert.AlertType.ERROR, "Nome vazio!", ButtonType.OK
				);
				alert.setHeaderText("Impossível adicionar entrada!");
				alert.show();
			}else if (!DocumentValidator.isValidCNPJ(entry.get().getCpfcnpj())) {
				Alert alert = new Alert(
					Alert.AlertType.ERROR, "CNPJ inválido!", ButtonType.OK
				);
				alert.setHeaderText("Impossível adicionar entrada!");
				alert.show();
			}else{
				dataManager.addEntry(entry.get());
			}
		}
		refreshTable();

	}

	private void refreshTable() {
		blacklisted.addAll(dataManager.requestAllEntries());
	}

}

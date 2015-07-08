package com.kasama.permablacklist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

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

	@FXML
	private void addEntry() {

		NewEntryDialog newEntryDialog = new NewEntryDialog();
		Optional<BlacklistEntry> OPEntry = Optional.empty();
		try {
			OPEntry = newEntryDialog.showAndWait();
		}catch(Throwable ignored){}

		if (OPEntry.isPresent()) {
			if (OPEntry.get().getName().equals("")) {
				Alert alert = new Alert(
					Alert.AlertType.ERROR, "Nome vazio!", ButtonType.OK
				);
				alert.setHeaderText("Impossível adicionar entrada!");
				alert.show();
			} else if (!DocumentValidator.isValidCNPJ(
				DocumentValidator.numberOnlyCPFCNPJ(
					OPEntry.get().getCpfcnpj()
				)
			)) {
				Alert alert = new Alert(
					Alert.AlertType.ERROR, "CNPJ inválido!", ButtonType.OK
				);
				alert.setHeaderText("Impossível adicionar entrada!");
				alert.show();
			} else {
				BlacklistEntry entry = OPEntry.get();
				entry.setCpfcnpj(DocumentValidator.parseToCNPJ(DocumentValidator.numberOnlyCPFCNPJ(entry.getCpfcnpj())));
				dataManager.addEntry(entry);
			}
		}
		refreshTable();

	}

	@FXML
	private void refreshTable() {
		List<BlacklistEntry> refreshing = dataManager.requestAllEntries();
		blacklisted.removeIf(all -> true);
		if (refreshing.size() > 0)
			blacklisted.addAll(dataManager.requestAllEntries());
	}

	@Override
	public void initialize(
		URL location, ResourceBundle resources
	) {
		dataManager = new DataManager("db/database.bin");
		initializeComponents();
	}
}

package com.kasama.permablacklist;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.io.IOException;

public class NewEntryDialog extends Dialog<BlacklistEntry> {

	TextField name;
	TextField cpfcnpj;

	public NewEntryDialog() {

		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("newEntryDialog.fxml")
		);
		loader.setController(this);

		ButtonType buttonCancel = new ButtonType(
			"Cancel", ButtonBar.ButtonData.CANCEL_CLOSE
		);
		ButtonType buttonOK = new ButtonType(
			"Confirm", ButtonBar.ButtonData.OK_DONE
		);
		Parent root;
		try {
			root = loader.load();
			this.getDialogPane().setContent(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getDialogPane().getButtonTypes().addAll(
			buttonCancel, buttonOK
		);
		this.setResultConverter(
			param -> param.equals(buttonOK) ? new BlacklistEntry(
				NewEntryDialog.this.getName(),
				NewEntryDialog.this.getCPFCNJP()
			) : null
		);

	}

	public String getName() {
		return name.getText();
	}

	public String getCPFCNJP() {
		return cpfcnpj.getText();
	}
}

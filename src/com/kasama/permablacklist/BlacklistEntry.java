package com.kasama.permablacklist;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BlacklistEntry {

	StringProperty name;
	StringProperty cpfcnpj;

	public BlacklistEntry(String name, String cpfcnpj){
		this.name = new SimpleStringProperty(name);
		this.cpfcnpj = new SimpleStringProperty(cpfcnpj);
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getCpfcnpj() {
		return cpfcnpj.get();
	}

	public StringProperty cpfcnpjProperty() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj.set(cpfcnpj);
	}
}

package com.kasama.permablacklist;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static javafx.scene.input.KeyCode.*;

/**
 * @author rafael
 */
public abstract class TextFieldMasker {

	private static List<KeyCode> ignoreKeyCodes;

	static {
		ignoreKeyCodes = new ArrayList<>();
		Collections.addAll(
			ignoreKeyCodes, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12
		);
	}

	public static void ignoreKeys(final TextField textField) {
		textField.addEventFilter(
			KeyEvent.KEY_PRESSED, keyEvent -> {
				if (ignoreKeyCodes.contains(keyEvent.getCode())) {
					keyEvent.consume();
				}
			}
		);
	}

	/**
	 * Monta a mascara para Data (dd/MM/yyyy).
	 *
	 * @param textField TextField
	 */
	public static void dateField(final TextField textField) {
		maxField(textField, 10);

		textField.lengthProperty().addListener(
			(observable, oldValue, newValue) -> {
				if (newValue.intValue() < 11) {
					textField.positionCaret(textField.getText().length()-2);
					String value = textField.getText();
					value = value.replaceAll("[^0-9]", "");
					value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
					value = value.replaceFirst(
						"(\\d{2})/(\\d{2})(\\d)", "$1/$2/$3"
					);
					textField.setText(value);
					textField.positionCaret(textField.getText().length()-1);
				}
			}
		);
	}

	/**
	 * Monta a mascara para Data (dd/MM/yyyy).
	 *
	 * @param textField TextField
	 */
	public static void cepField(final TextField textField) {
		maxField(textField, 9);

		textField.lengthProperty().addListener(
			(observable, oldValue, newValue) -> {
				if (newValue.intValue() < 10) {
					String value = textField.getText();
					value = value.replaceAll("[^0-9]", "");
					value = value.replaceFirst("(\\d{5})(\\d)", "$1-$2");
					textField.setText(value);
					textField.positionCaret(textField.getText().length()-1);
				}
			}
		);
	}

	/**
	 * Campo que aceita somente numericos.
	 *
	 * @param textField TextField
	 */
	public static void numericField(final TextField textField) {
		textField.lengthProperty().addListener(
			(observable, oldValue, newValue) -> {
				if (newValue.intValue() > oldValue.intValue()) {
					char ch = textField.getText().charAt(oldValue.intValue());
					if (!(ch >= '0' && ch <= '9')) {
						textField.setText(
							textField.getText().substring(
								0, textField.getText().length() - 1
							)
						);
					}
				}
			}
		);
	}

	/**
	 * Monta a mascara para Moeda.
	 *
	 * @param textField TextField
	 */
	public static void monetaryField(final TextField textField) {
		textField.setAlignment(Pos.CENTER_RIGHT);
		textField.lengthProperty().addListener(
			(observable, oldValue, newValue) -> {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceAll("([0-9])([0-9]{14})$", "$1.$2");
				value = value.replaceAll("([0-9])([0-9]{11})$", "$1.$2");
				value = value.replaceAll("([0-9])([0-9]{8})$", "$1.$2");
				value = value.replaceAll("([0-9])([0-9]{5})$", "$1.$2");
				value = value.replaceAll("([0-9])([0-9]{2})$", "$1,$2");
				textField.setText(value);
				textField.positionCaret(textField.getText().length()-1);

				maxField(textField, 17);
			}
		);

		textField.focusedProperty().addListener(
			(observableValue, aBoolean, fieldChange) -> {
				if (!fieldChange) {
					final int length = textField.getText().length();
					if (length > 0 && length < 3) {
						textField.setText(textField.getText() + "00");
					}
				}
			}
		);
	}

	/**
	 * Monta as mascara para CPF/CNPJ. A mascara eh exibida somente apos o campo perder o foco.
	 *
	 * @param textField TextField
	 */
	public static void cpfCnpjField(final TextField textField) {

		textField.focusedProperty().addListener(
			(observableValue, aBoolean, fieldChange) -> {
				String value = textField.getText();
				if (!fieldChange) {
					if (textField.getText().length() == 11) {
						value = value.replaceAll("[^0-9]", "");
						value = value.replaceFirst(
							"([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})$",
							"$1.$2.$3-$4"
						);
					}
					if (textField.getText().length() == 14) {
						value = value.replaceAll("[^0-9]", "");
						value = value.replaceFirst(
							"([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})$",
							"$1.$2.$3/$4-$5"
						);
					}
				}
				textField.setText(value);
				if (!Objects.equals(textField.getText(), value)) {
					textField.setText("");
					textField.insertText(0, value);
				}

			}
		);

		maxField(textField, 18);
	}

	/**
	 * Monta a mascara para os campos CNPJ.
	 *
	 * @param textField TextField
	 */
	public static void cnpjField(final TextField textField) {
		maxField(textField, 18);

		textField.textProperty().addListener(
			(observableValue, number, number2) -> {
				textField.positionCaret(textField.getText().length()-2);
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
				value = value.replaceFirst(
					"(\\d{2})\\.(\\d{3})(\\d)", "$1.$2.$3"
				);
				value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
				value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
				textField.setText(value);
				textField.positionCaret(textField.getText().length()-1);
			}
		);

	}

	/**
	 * Devido ao incremento dos caracteres das mascaras eh necessario que o cursor sempre se posicione no final da string.
	 *
	 * @param textField TextField
	 */
	private static void positionCaret(final TextField textField) {
		Platform.runLater(
			() -> textField.positionCaret(textField.getText().length())
		);
	}

	private static void maxField(
		final TextField textField, final Integer length
	) {
		textField.textProperty().addListener(
			(observableValue, oldValue, newValue) -> {
				if (newValue.length() > length) textField.setText(oldValue);
			}
		);
	}

}


package com.kasama.permablacklist;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class TextFieldMasker {
	/**
	 * Adds a static mask to the specified text field.
	 *
	 * @param tf   the text field.
	 * @param mask the mask to apply.
	 *             Example of usage: addMask(txtDate, "  /  /    ");
	 */
	public static void addMask(final TextField tf, final String mask) {
		tf.setText(mask);
		addTextLimiter(tf, mask.length());

		tf.textProperty().addListener(
			(ov, oldValue, newValue) -> {
				String value = stripMask(tf.getText(), mask);
				tf.setText(merge(value, mask));
			}
		);

		tf.setOnKeyPressed(
			e -> {
				int caretPosition = tf.getCaretPosition();
				if (caretPosition < mask.length() - 1 && mask.charAt(
					caretPosition
				) != ' ' && e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.LEFT) {
					tf.positionCaret(caretPosition + 1);
				}
			}
		);
	}

	static String merge(final String value, final String mask) {
		final StringBuilder sb = new StringBuilder(mask);
		int k = 0;
		for (int i = 0; i < mask.length(); i++) {
			if (mask.charAt(i) == ' ' && k < value.length()) {
				sb.setCharAt(i, value.charAt(k));
				k++;
			}
		}
		return sb.toString();
	}

	static String stripMask(String text, final String mask) {
		final Set<String> maskChars = new HashSet<>();
		for (int i = 0; i < mask.length(); i++) {
			char c = mask.charAt(i);
			if (c != ' ') {
				maskChars.add(String.valueOf(c));
			}
		}
		for (String c : maskChars) {
			text = text.replace(c, "");
		}
		return text;
	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(
			(ov, oldValue, newValue) -> {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		);
	}
}

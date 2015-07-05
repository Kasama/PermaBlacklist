package com.kasama.permablacklist;

public class DocumentValidator {
	private static final int[] CPFWeight =
		{11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int[] CNPJWeight =
		{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	private static int calcDigit(String str, int[] weight) {
		int sum = 0;
		for (int i = str.length() - 1, digit; i >= 0; i--) {
			digit = Integer.parseInt(str.substring(i, i + 1));
			sum += digit * weight[weight.length - str.length() + i];
		}
		sum = 11 - sum % 11;
		return sum > 9 ? 0 : sum;
	}

	public static boolean isValidCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != 11)) return false;

		Integer digit1 = calcDigit(cpf.substring(0, 9), CPFWeight);
		Integer digit2 = calcDigit(
			cpf.substring(0, 9) + digit1, CPFWeight
		);
		return cpf.equals(
			cpf.substring(0, 9) + digit1.toString() + digit2.toString()
		);
	}

	public static boolean isValidCNPJ(String cnpj) {
		if ((cnpj == null) || (cnpj.length() != 14)) return false;

		Integer digit1 = calcDigit(cnpj.substring(0, 12), CNPJWeight);
		Integer digit2 = calcDigit(
			cnpj.substring(0, 12) + digit1, CNPJWeight
		);
		return cnpj.equals(
			cnpj.substring(0, 12) + digit1.toString() + digit2.toString()
		);
	}

}

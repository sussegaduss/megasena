package com.milhao;

public class RegexUtil {

    private static final String REGEX_OR = "|";

    static String numeroSorteado(int dezena) {
        String inicioDeLinha = "^" + dezena + ",.*" + REGEX_OR + "^0" + dezena + ",.*";
        String finalDeLinha = ".*," + dezena + "$" + REGEX_OR + ".*,0" + dezena + "$";
        String meioDeLinha = ".*," + dezena + ",.*" + REGEX_OR + ".*,0" + dezena + ",.*";
        return inicioDeLinha + REGEX_OR + finalDeLinha + REGEX_OR + meioDeLinha;
    }
}
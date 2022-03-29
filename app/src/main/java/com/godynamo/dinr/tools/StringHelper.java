package com.godynamo.dinr.tools;

/**
 * Created by dankovassev on 2015-02-11.
 */
class StringHelper {
    public static String getDigitsOnlyString(String numString) {
        StringBuilder sb = new StringBuilder();
        for (char c : numString.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
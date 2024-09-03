package me.kisters.ciweda.util;

public class StringHelper {

    public static String formatNumberWithSpaces(long number) {
        String numberStr = String.valueOf(number);
        return numberStr.replaceAll("(?<=\\d)(?=(\\d{3})+$)", " ");
    }
}

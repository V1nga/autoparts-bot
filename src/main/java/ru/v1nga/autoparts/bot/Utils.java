package ru.v1nga.autoparts.bot;

public final class Utils {
    public static String pluralize(int count, String one, String few, String many) {
        int mod10 = count % 10;
        int mod100 = count % 100;

        if (mod10 == 1 && mod100 != 11) {
            return one;
        } else if (mod10 >= 2 && mod10 <= 4 && (mod100 < 12 || mod100 > 14)) {
            return few;
        } else {
            return many;
        }
    }
}

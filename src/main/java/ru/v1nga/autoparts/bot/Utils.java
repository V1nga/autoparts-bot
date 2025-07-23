package ru.v1nga.autoparts.bot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.HashMap;
import java.util.Map;

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

    public static String getCallbackData(CallbackQuery callbackQuery) {
        String[] callbackData = callbackQuery.getData().split(":");

        return callbackData.length > 1 ? callbackData[1] : null;
    }

    public static String getCallbackParam(CallbackQuery callbackQuery, String paramName) {
        String query = getCallbackData(callbackQuery);

        if(query != null) {
            Map<String, String> params = new HashMap<>();

            for (String pair : query.split("&")) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2) {
                    params.put(parts[0], parts[1]);
                }
            }

            return params.get(paramName);
        } else {
            return null;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

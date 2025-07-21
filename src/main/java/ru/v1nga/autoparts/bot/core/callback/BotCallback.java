package ru.v1nga.autoparts.bot.core.callback;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BotCallback implements IBotCallback {

    private final String callbackIdentifier;

    public final String getCallbackIdentifier() {
        return this.callbackIdentifier;
    }

    protected String getCallbackParam(CallbackQuery callbackQuery) {
        String[] callbackData = callbackQuery.getData().split(":");

        return callbackData.length > 1 ? callbackData[1] : null;
    }

    protected String getCallbackNamedParam(CallbackQuery callbackQuery, String paramName) {
        String query = getCallbackParam(callbackQuery);

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

    public abstract void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery);
}

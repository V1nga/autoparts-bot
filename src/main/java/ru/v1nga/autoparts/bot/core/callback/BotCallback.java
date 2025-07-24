package ru.v1nga.autoparts.bot.core.callback;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.Utils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BotCallback implements IBotCallback {

    private final String callbackIdentifier;

    public final String getCallbackIdentifier() {
        return this.callbackIdentifier;
    }

    protected String getCallbackData(CallbackQuery callbackQuery) {
        return Utils.getCallbackData(callbackQuery);
    }

    protected String getCallbackParam(CallbackQuery callbackQuery, String paramName) {
       return Utils.getCallbackParam(callbackQuery, paramName);
    }

    public abstract void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery);
}

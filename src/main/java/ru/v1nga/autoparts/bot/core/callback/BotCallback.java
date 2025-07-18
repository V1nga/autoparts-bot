package ru.v1nga.autoparts.bot.core.callback;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
public abstract class BotCallback implements IBotCallback {

    private final String callbackIdentifier;

    public final String getCallbackIdentifier() {
        return this.callbackIdentifier;
    }

    public abstract void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery);
}

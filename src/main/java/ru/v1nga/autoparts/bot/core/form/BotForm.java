package ru.v1nga.autoparts.bot.core.form;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;

public abstract class BotForm<T> implements IBotForm {

    protected final Map<String, T> sessions;
    protected final TelegramClient telegramClient;

    public BotForm(Map<String, T> sessions, TelegramClient telegramClient) {
        this.sessions = sessions;
        this.telegramClient = telegramClient;
    }

    public abstract void startForm(String chatId);
    public abstract void handleInput(String chatId, String message);

    protected void send(String chatId, String text) {
        try {
            SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();

            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

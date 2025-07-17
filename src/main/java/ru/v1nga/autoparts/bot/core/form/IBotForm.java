package ru.v1nga.autoparts.bot.core.form;

import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface IBotForm {

    void start(String chatId);
    void handleInput(String chatId, String message);
    boolean isCompleted(String chatId);

    String getFormIdentifier();
}

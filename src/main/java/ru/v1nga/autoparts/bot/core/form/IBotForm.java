package ru.v1nga.autoparts.bot.core.form;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface IBotForm {

    void start(long chatId, CallbackQuery callbackQuery);
    void handleInput(long chatId, long userId, String message);
    boolean isCompleted(long chatId);

    String getFormIdentifier();
}

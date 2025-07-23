package ru.v1nga.autoparts.bot.core.form;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public interface IBotForm {

    void start(Chat chat, CallbackQuery callbackQuery);
    void handleInput(Chat chat, User user, String message);
    boolean isCompleted(Chat chat);

    String getFormIdentifier();
}

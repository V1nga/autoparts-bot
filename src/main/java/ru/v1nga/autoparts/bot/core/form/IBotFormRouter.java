package ru.v1nga.autoparts.bot.core.form;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public interface IBotFormRouter {
    void registerForm(BotForm botForm);
    void startForm(Chat chat, String formName, CallbackQuery callbackQuery);
    void handleInput(Chat chat, User user, String message);
}

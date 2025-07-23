package ru.v1nga.autoparts.bot.core.form;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface IBotFormRouter {
    void registerForm(BotForm botForm);
    void startForm(long chatId, String formName, CallbackQuery callbackQuery);
    void handleInput(long chatId, long userId, String message);
}

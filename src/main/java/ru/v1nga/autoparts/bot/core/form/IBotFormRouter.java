package ru.v1nga.autoparts.bot.core.form;

public interface IBotFormRouter {
    void registerForm(BotForm botForm);
    void startForm(long chatId, String formName);
    void handleInput(long chatId, String message);
}

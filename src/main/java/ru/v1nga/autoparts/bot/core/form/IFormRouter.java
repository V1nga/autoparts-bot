package ru.v1nga.autoparts.bot.core.form;

public interface IFormRouter {
    void registerForm(BotForm botForm);
    void startForm(String chatId, String formName);
    void handleInput(String chatId, String message);
}

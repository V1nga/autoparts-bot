package ru.v1nga.autoparts.bot.core.form;

public interface IBotForm {

    void start(long chatId);
    void handleInput(long chatId, String message);
    boolean isCompleted(long chatId);

    String getFormIdentifier();
}

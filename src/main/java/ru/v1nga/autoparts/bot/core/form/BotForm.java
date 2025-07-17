package ru.v1nga.autoparts.bot.core.form;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
public abstract class BotForm implements IBotForm {

    private final String formIdentifier;
    private final TelegramClient telegramClient;

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }

    protected void send(String chatId, String text) {
        try {
            telegramClient.execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

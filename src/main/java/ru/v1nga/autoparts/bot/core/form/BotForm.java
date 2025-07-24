package ru.v1nga.autoparts.bot.core.form;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public abstract class BotForm implements IBotForm {

    private final String formIdentifier;
    private final TelegramClient telegramClient;

    private final Map<Long, BotFormSession> sessions = new ConcurrentHashMap<>();

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }

    protected BotFormSession getSession(long chatId) {
        return sessions.get(chatId);
    }

    protected void setSession(long chatId, BotFormSession botFormSession) {
        sessions.put(chatId, botFormSession);
    }

    protected void send(long chatId, String message) {
        send(SendMessage.builder().chatId(chatId).text(message).build());
    }

    protected void sendError(long chatId, String message) {
        send(
            SendMessage
                .builder()
                .chatId(chatId)
                .text(
                    EmojiParser.parseToUnicode(":warning: " + message)
                )
                .build()
        );
    }

    protected void send(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void send(EditMessageText message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

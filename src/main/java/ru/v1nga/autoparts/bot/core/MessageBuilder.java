package ru.v1nga.autoparts.bot.core;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

public abstract class MessageBuilder {

    protected SendMessage buildMessage(long chatId, String text, List<InlineKeyboardRow> keyboard) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup
                    .builder()
                    .keyboard(keyboard)
                    .build())
                .build();
    }
}

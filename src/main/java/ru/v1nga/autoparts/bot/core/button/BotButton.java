package ru.v1nga.autoparts.bot.core.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

public abstract class BotButton implements IBotButton {

    @Override
    public InlineKeyboardRow getRow() {
        return new InlineKeyboardRow(List.of(get()));
    }
}

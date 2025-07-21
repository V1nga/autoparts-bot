package ru.v1nga.autoparts.bot.core.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public interface IBotButton {
    InlineKeyboardButton get();
    InlineKeyboardRow getRow();
}

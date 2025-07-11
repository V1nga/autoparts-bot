package ru.v1nga.autoparts.bot.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Menu {
    SendMessage build(long chatId);
}

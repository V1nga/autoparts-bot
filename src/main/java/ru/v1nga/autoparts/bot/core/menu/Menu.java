package ru.v1nga.autoparts.bot.core.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.v1nga.autoparts.bot.core.MessageBuilder;

public abstract class Menu extends MessageBuilder {
    public abstract SendMessage build(long chatId);
}

package ru.v1nga.autoparts.bot.core.card;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.v1nga.autoparts.bot.core.MessageBuilder;

public abstract class Card<T> extends MessageBuilder {
    public abstract SendMessage build(long chatId, T entity);
}

package ru.v1nga.autoparts.bot.core.card;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.v1nga.autoparts.bot.core.MessageComposer;

public abstract class Card<T> extends MessageComposer {
    public abstract SendMessage compose(long chatId, T entity);
    public abstract EditMessageText compose(long chatId, int messageId, T entity);
}

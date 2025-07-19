package ru.v1nga.autoparts.bot.core.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BotFormSession implements IBotFormSession {
    private boolean complete;
}

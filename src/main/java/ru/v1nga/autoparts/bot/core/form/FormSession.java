package ru.v1nga.autoparts.bot.core.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FormSession implements IFormSession {
    private String currentStep;
}

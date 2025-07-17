package ru.v1nga.autoparts.bot.core.form;

import java.util.HashMap;
import java.util.Map;

public class FormRouter {

    private final Map<String, BotForm<FormSession>> activeForms = new HashMap<>();
}

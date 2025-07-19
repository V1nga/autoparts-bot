package ru.v1nga.autoparts.bot.core.form;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotFormRouter implements IBotFormRouter {

    private final Map<String, IBotForm> forms = new ConcurrentHashMap<>();
    private final Map<Long, IBotForm> activeForms = new ConcurrentHashMap<>();

    @Override
    public void registerForm(BotForm botForm) {
        forms.put(botForm.getFormIdentifier(), botForm);
    }

    @Override
    public void startForm(long chatId, String formName) {
        IBotForm form = forms.get(formName);

        if(form != null) {
            activeForms.put(chatId, form);
            form.start(chatId);
        }
    }

    @Override
    public void handleInput(long chatId, String message) {
        IBotForm form = activeForms.get(chatId);

        if(form != null) {
            form.handleInput(chatId, message);

            if(form.isCompleted(chatId)) {
                activeForms.remove(chatId);
            }
        }
    }
}

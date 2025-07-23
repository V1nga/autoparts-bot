package ru.v1nga.autoparts.bot.core.form;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

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
    public void startForm(long chatId, String formName, CallbackQuery callbackQuery) {
        IBotForm form = forms.get(formName);

        if(form != null) {
            activeForms.put(chatId, form);
            form.start(chatId, callbackQuery);
        }
    }

    @Override
    public void handleInput(long chatId, long userId, String message) {
        IBotForm form = activeForms.get(chatId);

        if(form != null) {
            form.handleInput(chatId, userId, message);

            if(form.isCompleted(chatId)) {
                activeForms.remove(chatId);
            }
        }
    }
}

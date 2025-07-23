package ru.v1nga.autoparts.bot.core.form;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

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
    public void startForm(Chat chat, String formName, CallbackQuery callbackQuery) {
        IBotForm form = forms.get(formName);

        if(form != null) {
            activeForms.put(chat.getId(), form);
            form.start(chat, callbackQuery);
        }
    }

    @Override
    public void handleInput(Chat chat, User user, String message) {
        IBotForm form = activeForms.get(chat.getId());

        if(form != null) {
            form.handleInput(chat, user, message);

            if(form.isCompleted(chat)) {
                activeForms.remove(chat.getId());
            }
        }
    }
}

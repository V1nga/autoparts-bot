package ru.v1nga.autoparts.bot.forms;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.form.BotForm;

import java.util.HashMap;
import java.util.Map;

@Component
public class SearchForm extends BotForm {

    private final Map<String, SearchFormSession> sessions = new HashMap<>();

    public SearchForm(TelegramClient telegramClient) {
        super("search", telegramClient);
    }

    @Override
    public void start(String chatId) {
        SearchFormSession session = new SearchFormSession();
        session.setCurrentStep("ASK_NAME");
        sessions.put(chatId, session);
        send(chatId, "Введите ваше имя:");
    }

    @Override
    public void handleInput(String chatId, String message) {
        SearchFormSession session = sessions.get(chatId);

        if (session == null) {
            return;
        }

        switch (session.getCurrentStep()) {
            case "ASK_NAME":
                session.setName(message);
                session.setCurrentStep("ASK_AGE");
                send(chatId, "Сколько вам лет?");
                break;
            case "ASK_AGE":
                session.setAge(message);
                session.setCurrentStep("DONE");
                send(chatId, "Готово! Имя: " + session.getName() + ", возраст: " + session.getAge());
                break;
        }
    }

    @Override
    public boolean isCompleted(String chatId) {
        SearchFormSession session = sessions.get(chatId);

        return session != null && "DONE".equals(session.getCurrentStep());
    }
}

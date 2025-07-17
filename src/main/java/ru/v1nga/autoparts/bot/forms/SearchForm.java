package ru.v1nga.autoparts.bot.forms;

import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.form.BotForm;

import java.util.Map;

public class SearchForm extends BotForm<SearchFormSession> {

    public SearchForm(Map<String, SearchFormSession> sessions, TelegramClient telegramClient) {
        super(sessions, telegramClient);
    }

    @Override
    public String getFormIdentifier() {
        return "";
    }

    @Override
    public void startForm(String chatId) {
        SearchFormSession session = new SearchFormSession();
        session.setCurrentStep("ASK_NAME");
        sessions.put(chatId, session);
        send(chatId, "Привет! Как вас зовут?");
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
                session.setCurrentStep("COMPLETED");
                send(chatId, "Спасибо! Вы ввели:\nИмя: " + session.getName() + "\nВозраст: " + session.getAge());
                break;
        }
    }
}

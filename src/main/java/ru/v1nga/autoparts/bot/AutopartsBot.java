package ru.v1nga.autoparts.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.v1nga.autoparts.bot.commands.StartCommand;

@Component
public class AutopartsBot extends CommandLongPollingTelegramBot implements SpringLongPollingBot {

    private final String botToken;

    @Autowired
    private StartCommand startCommand;

    public AutopartsBot(@Value("${bot.token}") String botToken, @Value("${bot.name}") String botName) {
        super(new OkHttpTelegramClient(botToken), true, () -> botName);
        this.botToken = botToken;
    }

    @PostConstruct
    public void init() {
        register(startCommand);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage(String.valueOf(message.getChatId()), "Hey heres your message:\n" + message.getText());
                try {
                    telegramClient.execute(echoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();;
                }
            }
        }
    }
}

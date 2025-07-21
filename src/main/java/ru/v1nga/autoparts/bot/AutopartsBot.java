package ru.v1nga.autoparts.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.callbacks.AddCartCallback;
import ru.v1nga.autoparts.bot.callbacks.GetCartCallback;
import ru.v1nga.autoparts.bot.callbacks.GetMenuCallback;
import ru.v1nga.autoparts.bot.callbacks.SearchCallback;
import ru.v1nga.autoparts.bot.commands.StartCommand;
import ru.v1nga.autoparts.bot.core.CallbackCommandLongPollingTelegramBot;
import ru.v1nga.autoparts.bot.forms.SearchForm;

@Component
public class AutopartsBot extends CallbackCommandLongPollingTelegramBot {

    private final String botToken;

    @Autowired
    private StartCommand startCommand;

    @Autowired
    private SearchCallback searchCallback;
    @Autowired
    private AddCartCallback addCartCallback;
    @Autowired
    private GetCartCallback getCartCallback;
    @Autowired
    private GetMenuCallback getMenuCallback;

    @Autowired
    private SearchForm searchForm;

    public AutopartsBot(TelegramClient telegramClient, @Value("${bot.token}") String botToken, @Value("${bot.name}") String botName) {
        super(telegramClient, true, () -> botName);
        this.botToken = botToken;
    }

    @PostConstruct
    public void init() {
        register(startCommand);

        registerCallback(searchCallback);
        registerCallback(addCartCallback);
        registerCallback(getCartCallback);
        registerCallback(getMenuCallback);

        registerForm(searchForm);
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
    public void processNonCommandOrCallbackUpdate(Update update) {
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

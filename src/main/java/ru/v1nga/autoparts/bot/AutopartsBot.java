package ru.v1nga.autoparts.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.callbacks.*;
import ru.v1nga.autoparts.bot.commands.StartCommand;
import ru.v1nga.autoparts.bot.core.CallbackCommandLongPollingTelegramBot;
import ru.v1nga.autoparts.bot.forms.AddCartForm;
import ru.v1nga.autoparts.bot.forms.InformationFillForm;
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
    private DeleteFromCartCallback deleteFromCartCallback;
    @Autowired
    private ClearCartCallback clearCartCallback;
    @Autowired
    private GetCartCallback getCartCallback;
    @Autowired
    private GetMenuCallback getMenuCallback;
    @Autowired
    private GetPartDetailsCallback getPartDetailsCallback;
    @Autowired
    private InformationFillCallback informationFillCallback;
    @Autowired
    private MakeOrderCallback makeOrderCallback;
    @Autowired
    private GetOrderCallback getOrderCallback;
    @Autowired
    private GetOrdersCallback getOrdersCallback;

    @Autowired
    private SearchForm searchForm;
    @Autowired
    private AddCartForm addCartForm;
    @Autowired
    private InformationFillForm informationFillForm;


    public AutopartsBot(TelegramClient telegramClient, @Value("${bot.token}") String botToken, @Value("${bot.name}") String botName) {
        super(telegramClient, true, () -> botName);
        this.botToken = botToken;
    }

    @PostConstruct
    public void init() {
        register(startCommand);

        registerCallback(searchCallback);
        registerCallback(addCartCallback);
        registerCallback(deleteFromCartCallback);
        registerCallback(clearCartCallback);
        registerCallback(getCartCallback);
        registerCallback(getMenuCallback);
        registerCallback(getPartDetailsCallback);
        registerCallback(informationFillCallback);
        registerCallback(makeOrderCallback);
        registerCallback(getOrderCallback);
        registerCallback(getOrdersCallback);

        registerForm(searchForm);
        registerForm(addCartForm);
        registerForm(informationFillForm);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}

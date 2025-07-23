package ru.v1nga.autoparts.bot.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.bot.core.exceptions.CallbackNotFoundException;
import ru.v1nga.autoparts.bot.core.form.BotForm;
import ru.v1nga.autoparts.bot.core.form.BotFormRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public abstract class CallbackCommandLongPollingTelegramBot extends CommandLongPollingTelegramBot implements SpringLongPollingBot {

    @Autowired
    private BotFormRouter botFormRouter;

    private final List<BotCallback> callbacks = new ArrayList<>();

    public CallbackCommandLongPollingTelegramBot(TelegramClient telegramClient, boolean allowCommandsWithUsername, Supplier<String> botUsernameSupplier) {
        super(telegramClient, allowCommandsWithUsername, botUsernameSupplier);
    }

    public final void registerForm(BotForm botForm) {
        botFormRouter.registerForm(botForm);
    }
    public final void registerCallback(BotCallback botCallback) {
        callbacks.add(botCallback);
    }

    @Override
    public final void processNonCommandUpdate(Update update) {
        if(update.hasCallbackQuery()) {
            BotCallback callback = callbacks.stream()
                .filter(botCallback -> botCallback.getCallbackIdentifier().equals(update.getCallbackQuery().getData().split(":")[0]))
                .findFirst()
                .orElseThrow(() -> new CallbackNotFoundException("Callback \"" + update.getCallbackQuery().getData() + "\" not found"));

            callback.execute(
                this.telegramClient,
                update.getCallbackQuery().getFrom(),
                update.getCallbackQuery().getMessage().getChat(),
                update.getCallbackQuery()
            );
        } else {
            if (update.hasMessage() && update.getMessage().hasText()) {
                botFormRouter.handleInput(update.getMessage().getChat(), update.getMessage().getFrom(), update.getMessage().getText());
            }
        }
    }

    public abstract void processNonCommandOrCallbackUpdate(Update update);
}

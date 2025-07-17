package ru.v1nga.autoparts.bot.core;

import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.bot.core.exceptions.CallbackNotFoundException;
import ru.v1nga.autoparts.bot.core.form.BotForm;
import ru.v1nga.autoparts.bot.core.form.FormSession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class CallbackCommandLongPollingTelegramBot extends CommandLongPollingTelegramBot implements SpringLongPollingBot {

    private final List<BotForm<FormSession>> forms = new ArrayList<>();
    private final List<BotCallback> callbacks = new ArrayList<>();

    public CallbackCommandLongPollingTelegramBot(TelegramClient telegramClient, boolean allowCommandsWithUsername, Supplier<String> botUsernameSupplier) {
        super(telegramClient, allowCommandsWithUsername, botUsernameSupplier);
    }

    public final void registerForm(BotForm<FormSession> botForm) {
        forms.add(botForm);
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
                String chatId = update.getMessage().getChatId().toString();
                String text = update.getMessage().getText();
                formHandler.handleInput(chatId, text);
            }
//            this.processNonCommandOrCallbackUpdate(update);
        }
    }

    public abstract void processNonCommandOrCallbackUpdate(Update update);
}

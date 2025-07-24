package ru.v1nga.autoparts.bot.callbacks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.bot.core.form.BotFormRouter;

@Component
public class InformationFillCallback extends BotCallback {

    private final BotFormRouter botFormRouter;

    public InformationFillCallback(BotFormRouter botFormRouter) {
        super("information-fill");
        this.botFormRouter = botFormRouter;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        botFormRouter.startForm(chat, "information-fill", callbackQuery);
    }
}

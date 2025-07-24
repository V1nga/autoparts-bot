package ru.v1nga.autoparts.bot.callbacks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.bot.core.form.BotFormRouter;

@Component
public class AddCartCallback extends BotCallback {

    private final BotFormRouter botFormRouter;

    public AddCartCallback(BotFormRouter botFormRouter) {
        super("add-to-cart");
        this.botFormRouter = botFormRouter;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        botFormRouter.startForm(chat, "add-cart", callbackQuery);
    }
}

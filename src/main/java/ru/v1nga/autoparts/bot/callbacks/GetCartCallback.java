package ru.v1nga.autoparts.bot.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.cards.PartCard;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.bot.menus.CartMenu;
import ru.v1nga.autoparts.entities.CartItemEntity;
import ru.v1nga.autoparts.repositories.CartItemsRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GetCartCallback extends BotCallback {

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private PartCard partCard;

    @Autowired
    private CartMenu cartMenu;

    public GetCartCallback() {
        super("get-cart");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            String callbackParam = getCallbackParam(callbackQuery);
            List<CartItemEntity> cartItemEntities = cartItemsRepository.findByUserId(user.getId());

            int page = callbackParam == null ? 1 : Integer.parseInt(callbackParam);
            EditMessageText cartMenuMessage = cartMenu
                .build(
                    chat.getId(),
                    callbackQuery.getMessage().getMessageId(),
                    page,
                    cartItemEntities
                );

            telegramClient.execute(cartMenuMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

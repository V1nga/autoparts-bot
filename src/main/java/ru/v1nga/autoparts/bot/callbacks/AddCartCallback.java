package ru.v1nga.autoparts.bot.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.entities.CartItemEntity;
import ru.v1nga.autoparts.entities.PartEntity;
import ru.v1nga.autoparts.repositories.CartItemsRepository;
import ru.v1nga.autoparts.repositories.PartsRepository;

@Component
public class AddCartCallback extends BotCallback {

    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private PartsRepository partsRepository;

    public AddCartCallback() {
        super("add-to-cart");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        String partNumber = callbackQuery.getData().split(":")[1];

        PartEntity partEntity = partsRepository.findByNumber(partNumber).orElseThrow();

        CartItemEntity cartItem = cartItemsRepository
            .findByUserIdAndPart(user.getId(), partEntity)
            .orElseGet(() -> new CartItemEntity(0, user.getId(), partEntity, 0));
        cartItem.setQuantity(cartItem.getQuantity() + 1);

        cartItemsRepository.save(cartItem);
    }
}

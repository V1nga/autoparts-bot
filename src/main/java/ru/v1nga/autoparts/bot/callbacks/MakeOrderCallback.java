package ru.v1nga.autoparts.bot.callbacks;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.buttons.OrdersButton;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.entities.CartItemEntity;
import ru.v1nga.autoparts.entities.OrderEntity;
import ru.v1nga.autoparts.entities.OrderItemEntity;
import ru.v1nga.autoparts.repositories.CartItemsRepository;
import ru.v1nga.autoparts.repositories.OrdersRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class MakeOrderCallback extends BotCallback {

    private final CartItemsRepository cartItemsRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    private HomeButton homeButton;
    @Autowired
    private OrdersButton ordersButton;

    public MakeOrderCallback(CartItemsRepository cartItemsRepository, OrdersRepository ordersRepository) {
        super("make-order");
        this.cartItemsRepository = cartItemsRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            List<CartItemEntity> cartItems = cartItemsRepository.findByUserId(user.getId());

            if(!cartItems.isEmpty()) {
                OrderEntity order = new OrderEntity(
                    0,
                    user.getId(),
                    OrderEntity.OrderStatus.NEW,
                    new ArrayList<>(),
                    null
                );
                order.setOrders(
                    cartItems
                        .stream()
                        .map(cartItem ->
                            new OrderItemEntity(0, cartItem.getPart(), cartItem.getQuantity(), order)
                        )
                        .toList()
                );

                ordersRepository.save(order);
                cartItemsRepository.deleteByUserId(user.getId());

                telegramClient.execute(EditMessageText
                    .builder()
                    .chatId(chat.getId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .text(
                        EmojiParser.parseToUnicode(
                            Utils.composeMultiline(
                                ":confetti_ball: Спасибо за заказ!",
                                ":package: Мы уже начали обработку вашего заказа."
                            )
                        )
                    )
                    .replyMarkup(
                        InlineKeyboardMarkup
                            .builder()
                            .keyboard(
                                List.of(
                                    ordersButton.getRow(),
                                    homeButton.getRow()
                                )
                            )
                            .build()
                    )
                    .build()
                );
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

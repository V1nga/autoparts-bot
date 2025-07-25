package ru.v1nga.autoparts.bot.callbacks;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.buttons.OrdersButton;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.entities.OrderEntity;
import ru.v1nga.autoparts.repositories.OrdersRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GetOrderCallback extends BotCallback {

    private final OrdersRepository ordersRepository;

    @Autowired
    private OrdersButton ordersButton;

    public GetOrderCallback(OrdersRepository ordersRepository) {
        super("get-order");
        this.ordersRepository = ordersRepository;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            String callbackParam = getCallbackData(callbackQuery);
            Optional<OrderEntity> orderEntity = ordersRepository.findById(Long.parseLong(callbackParam));

            if(orderEntity.isPresent()) {
                EditMessageText orderMessage = EditMessageText
                    .builder()
                    .chatId(chat.getId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .text(
                        Utils.composeMultiline(
                            2,
                            EmojiParser.parseToUnicode(":package: Заказ №" + orderEntity.get().getId()),
                            Utils.composeMultiline(
                                2,
                                orderEntity
                                    .get()
                                    .getOrders()
                                    .stream()
                                    .map(orderItem -> {
                                        String partNumber = orderItem.getPart().getNumber();
                                        String quantity = String.valueOf(orderItem.getQuantity());

                                        return EmojiParser.parseToUnicode(
                                            Utils.composeMultiline(
                                                ":wrench: Артикул: " + partNumber,
                                                ":input_numbers: Количество: " + quantity
                                            )
                                        );
                                    })
                                    .toArray(String[]::new)
                            )
                        )
                    )
                    .replyMarkup(
                        InlineKeyboardMarkup
                            .builder()
                            .keyboard(List.of(ordersButton.getRow()))
                            .build()
                    )
                    .build();

                telegramClient.execute(orderMessage);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

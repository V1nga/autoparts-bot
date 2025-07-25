package ru.v1nga.autoparts.bot.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.bot.menus.OrdersMenu;
import ru.v1nga.autoparts.entities.OrderEntity;
import ru.v1nga.autoparts.repositories.OrdersRepository;

import java.util.List;

@Component
public class GetOrdersCallback extends BotCallback {

    private final OrdersRepository ordersRepository;

    @Autowired
    private OrdersMenu ordersMenu;

    public GetOrdersCallback(OrdersRepository ordersRepository) {
        super("get-orders");
        this.ordersRepository = ordersRepository;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            String callbackParam = getCallbackData(callbackQuery);
            List<OrderEntity> orderEntities = ordersRepository.findByUserId(user.getId());

            int page = callbackParam == null ? 1 : Integer.parseInt(callbackParam);
            EditMessageText ordersMenuMessage = ordersMenu
                .compose(
                    chat.getId(),
                    callbackQuery.getMessage().getMessageId(),
                    page,
                    orderEntities
                );

            telegramClient.execute(ordersMenuMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

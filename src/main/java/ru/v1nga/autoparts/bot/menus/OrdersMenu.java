package ru.v1nga.autoparts.bot.menus;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.core.menu.PaginationMenu;
import ru.v1nga.autoparts.entities.OrderEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Component
public class OrdersMenu extends PaginationMenu<OrderEntity> {

    @Autowired
    private HomeButton homeButton;

    private String getTitle(List<OrderEntity> orderEntities) {
        String title = ":package: Заказы";

        if(orderEntities.isEmpty()) {
            return
                EmojiParser.parseToUnicode(
                    Utils.composeMultiline(
                        title,
                        "",
                        "Тут пока ничего нет! Закажите что-нибудь :blush:"
                    )
                );
        } else {
            return EmojiParser.parseToUnicode(title);
        }
    }

    private List<InlineKeyboardRow> getKeyboard(int page, List<OrderEntity> orderEntities) {
        InlineKeyboardRow paginationButtons = getPaginationButton(page, orderEntities.size());

        List<InlineKeyboardRow> buttons = getItems(page, orderEntities)
            .stream()
            .map(orderEntity ->
                new InlineKeyboardRow(
                    InlineKeyboardButton
                        .builder()
                        .text(
                            String.format(
                                "%s, Заказ №%d",
                                orderEntity
                                    .getCreatedAt()
                                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                                orderEntity.getId()
                            )
                        )
                        .callbackData("get-order:" + orderEntity.getId())
                        .build()
                )
            )
            .toList();

        List<InlineKeyboardRow> footerButtons = List.of(
            paginationButtons,
            homeButton.getRow()
        );

        return Stream.concat(buttons.stream(), footerButtons.stream()).toList();
    }

    @Override
    public SendMessage compose(long chatId, int page, List<OrderEntity> orderEntities) {
        return composeMessage(chatId, getTitle(orderEntities), getKeyboard(page, orderEntities));
    }

    @Override
    public EditMessageText compose(long chatId, int messageId, int page, List<OrderEntity> orderEntities) {
        return composeMessage(chatId, messageId, getTitle(orderEntities), getKeyboard(page, orderEntities));
    }
}

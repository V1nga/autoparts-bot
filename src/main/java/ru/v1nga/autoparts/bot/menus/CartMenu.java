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
import ru.v1nga.autoparts.entities.CartItemEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CartMenu extends PaginationMenu<CartItemEntity> {

    @Autowired
    private HomeButton homeButton;

    private String getTitle(List<CartItemEntity> cartItemEntities) {
        String title = ":shopping_cart: Корзина";

        if(cartItemEntities.isEmpty()) {
            return
                EmojiParser.parseToUnicode(
                    Utils.composeMultiline(
                        title,
                        "",
                        "Тут пока ничего нет! Добавьте что-нибудь :blush:"
                    )
                );
        } else {
            return EmojiParser.parseToUnicode(title);
        }
    }

    private List<InlineKeyboardRow> getKeyboard(int page, List<CartItemEntity> cartItemEntities) {
        InlineKeyboardRow paginationButtons = getPaginationButton(page, cartItemEntities.size());

        List<InlineKeyboardRow> buttons = getItems(page, cartItemEntities)
            .stream()
            .map(cartItemEntity ->
                List.of(
                    new InlineKeyboardRow(
                        InlineKeyboardButton
                            .builder()
                            .text(
                                String.format(
                                    "%dшт. %s (%s)",
                                    cartItemEntity.getQuantity(),
                                    cartItemEntity.getPart().getNumber(),
                                    cartItemEntity.getPart().getDescription()
                                )
                            )
                            .callbackData("test")
                            .build()
                    ),
                    new InlineKeyboardRow(
                        InlineKeyboardButton
                            .builder()
                            .text(
                                EmojiParser.parseToUnicode(
                                ":pencil2: Изменить кол-во"
                                )
                            )
                            .callbackData("add-to-cart:" + cartItemEntity.getPart().getNumber())
                            .build(),
                        InlineKeyboardButton
                            .builder()
                            .text(
                                EmojiParser.parseToUnicode(
                                ":x: Удалить"
                                )
                            )
                            .callbackData("delete-from-cart:" + cartItemEntity.getId())
                            .build()
                    ),
                    new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("\u200B").callbackData("_").build()
                    )
                )
            )
            .flatMap(Collection::stream)
            .toList();

        return Stream.concat(buttons.stream(), Stream.of(paginationButtons, homeButton.getRow())).toList();
    }

    @Override
    public SendMessage compose(long chatId, int page, List<CartItemEntity> cartItemEntities) {
        return composeMessage(chatId, getTitle(cartItemEntities), getKeyboard(page, cartItemEntities));
    }

    @Override
    public EditMessageText compose(long chatId, int messageId, int page, List<CartItemEntity> cartItemEntities) {
        return composeMessage(chatId, messageId, getTitle(cartItemEntities), getKeyboard(page, cartItemEntities));
    }
}

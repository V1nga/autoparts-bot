package ru.v1nga.autoparts.bot.menus;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.buttons.CartButton;
import ru.v1nga.autoparts.bot.buttons.OrdersButton;
import ru.v1nga.autoparts.bot.buttons.SearchButton;
import ru.v1nga.autoparts.bot.core.menu.Menu;

import java.util.List;

@Component
public class MainMenu extends Menu {

    @Autowired
    private CartButton cartButton;
    @Autowired
    private SearchButton searchButton;
    @Autowired
    private OrdersButton ordersButton;

    private String getTitle() {
        return EmojiParser.parseToUnicode(":gear: Магазин \"Autoparts\" :gear:");
    }

    private List<InlineKeyboardRow> getKeyboard() {
        return List.of(
            searchButton.getRow(),
            new InlineKeyboardRow(
                cartButton.get(),
                ordersButton.get()
            )
        );
    }

    @Override
    public SendMessage compose(long chatId) {
        return composeMessage(chatId, getTitle(), getKeyboard());
    }

    @Override
    public EditMessageText compose(long chatId, int messageId) {
        return composeMessage(chatId, messageId, getTitle(), getKeyboard());
    }
}

package ru.v1nga.autoparts.bot.menus;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.buttons.CartButton;
import ru.v1nga.autoparts.bot.core.menu.Menu;

import java.util.List;

@Component
public class MainMenu extends Menu {

    @Autowired
    private CartButton cartButton;

    private String getTitle() {
        return EmojiParser.parseToUnicode(":gear: Магазин \"Autoparts\" :gear:");
    }

    private List<InlineKeyboardRow> getKeyboard() {
        return List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton
                    .builder()
                    .text(EmojiParser.parseToUnicode(":mag: Поиск запчасти"))
                    .callbackData("search")
                    .build()
            ),
            new InlineKeyboardRow(
                cartButton.get(),
                InlineKeyboardButton
                    .builder()
                    .text(EmojiParser.parseToUnicode(":package: Заказы"))
                    .callbackData("search:test")
                    .build()
            )
        );
    }

    @Override
    public SendMessage build(long chatId) {
        return buildMessage(chatId, getTitle(), getKeyboard());
    }

    @Override
    public EditMessageText build(long chatId, int messageId) {
        return buildMessage(chatId, messageId, getTitle(), getKeyboard());
    }
}

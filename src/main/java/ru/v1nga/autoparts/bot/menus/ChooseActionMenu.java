package ru.v1nga.autoparts.bot.menus;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.core.menu.Menu;

import java.util.List;
import java.util.stream.Stream;

@Component
public class ChooseActionMenu extends Menu {

    private String getTitle() {
        return EmojiParser.parseToUnicode(":radio_button: Выберите действие или вернитесь в главное меню:");
    }

    private List<InlineKeyboardRow> getKeyboard() {
        return List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton
                    .builder()
                    .text(EmojiParser.parseToUnicode(":house: Главное меню"))
                    .callbackData("get-menu")
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

    public SendMessage build(long chatId, List<InlineKeyboardRow> buttons) {
        return buildMessage(chatId, getTitle(), Stream.concat(getKeyboard().stream(), buttons.stream()).toList());
    }

    public EditMessageText build(long chatId, int messageId, List<InlineKeyboardRow> buttons) {
        return buildMessage(chatId, messageId, getTitle(), Stream.concat(getKeyboard().stream(), buttons.stream()).toList());
    }
}

package ru.v1nga.autoparts.bot.core.menu;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.core.MessageBuilder;

import java.util.List;

public abstract class PaginationMenu<T> extends MessageBuilder {

    private final int itemsPerPage;

    public PaginationMenu() {
        this.itemsPerPage = 5;
    }
    public PaginationMenu(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    protected List<T> getItems(int page, List<T> allItems) {
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = startIndex + itemsPerPage;

        return allItems.subList(startIndex, Math.min(endIndex, allItems.size()));
    }

    protected InlineKeyboardRow getPaginationButton(int page, int totalItems) {
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        InlineKeyboardRow paginationButtons = new InlineKeyboardRow();
        InlineKeyboardButton prevButton = InlineKeyboardButton
                .builder()
                .text(EmojiParser.parseToUnicode(":arrow_left: Назад"))
                .callbackData("get-cart:" + (page - 1))
                .build();
        InlineKeyboardButton nextButton = InlineKeyboardButton
                .builder()
                .text(EmojiParser.parseToUnicode("Далее :arrow_right:"))
                .callbackData("get-cart:" + (page + 1))
                .build();

        if(page > 1) {
            paginationButtons.add(prevButton);
        }
        if(page < totalPages) {
            paginationButtons.add(nextButton);
        }

        return paginationButtons;
    }

    public abstract SendMessage build(long chatId, int page, List<T> items);
    public abstract EditMessageText build(long chatId, int messageId, int page, List<T> items);
}

package ru.v1nga.autoparts.bot.callbacks;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.buttons.CartButton;
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.repositories.CartItemsRepository;

import java.util.List;

@Component
public class DeleteFromCartCallback extends BotCallback {

    private final CartItemsRepository cartItemsRepository;

    @Autowired
    private HomeButton homeButton;
    @Autowired
    private CartButton cartButton;

    public DeleteFromCartCallback(CartItemsRepository cartItemsRepository) {
        super("delete-from-cart");
        this.cartItemsRepository = cartItemsRepository;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            String cartItemId = getCallbackData(callbackQuery);
            cartItemsRepository.deleteById(Long.parseLong(cartItemId));

            EditMessageText deleteMessage = EditMessageText.builder()
                .chatId(chat.getId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(
                    EmojiParser.parseToUnicode(":white_check_mark: Запчасть удалена из :shopping_cart:")
                )
                .replyMarkup(
                    InlineKeyboardMarkup.builder()
                        .keyboard(
                            List.of(
                                cartButton.getRow(),
                                homeButton.getRow()
                            )
                        )
                        .build()
                )
                .build();

            telegramClient.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

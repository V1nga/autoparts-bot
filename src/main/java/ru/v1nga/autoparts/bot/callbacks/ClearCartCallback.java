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
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.repositories.CartItemsRepository;

import java.util.List;

@Component
public class ClearCartCallback extends BotCallback {

    private final CartItemsRepository cartItemsRepository;

    @Autowired
    private HomeButton homeButton;

    public ClearCartCallback(CartItemsRepository cartItemsRepository) {
        super("clear-cart");
        this.cartItemsRepository = cartItemsRepository;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            cartItemsRepository.deleteByUserId(user.getId());

            EditMessageText clearCartMessage = EditMessageText.builder()
                .chatId(chat.getId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(
                    EmojiParser.parseToUnicode(":white_check_mark: Корзина очищена! :broom:")
                )
                .replyMarkup(
                    InlineKeyboardMarkup
                        .builder()
                        .keyboard(List.of(homeButton.getRow()))
                        .build()
                )
                .build();
            telegramClient.execute(clearCartMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

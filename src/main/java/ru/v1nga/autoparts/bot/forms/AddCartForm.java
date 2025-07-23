package ru.v1nga.autoparts.bot.forms;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.buttons.CartButton;
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.core.form.BotForm;
import ru.v1nga.autoparts.bot.core.form.BotFormSession;
import ru.v1nga.autoparts.entities.CartItemEntity;
import ru.v1nga.autoparts.entities.PartEntity;
import ru.v1nga.autoparts.repositories.CartItemsRepository;
import ru.v1nga.autoparts.repositories.PartsRepository;

import java.util.List;

@Component
public class AddCartForm extends BotForm {

    private final PartsRepository partsRepository;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    private HomeButton homeButton;
    @Autowired
    private CartButton cartButton;

    public AddCartForm(TelegramClient telegramClient, PartsRepository partsRepository, CartItemsRepository cartItemsRepository) {
        super("add-cart", telegramClient);
        this.partsRepository = partsRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Override
    public void start(long chatId, CallbackQuery callbackQuery) {
        AddCartFormSession session = new AddCartFormSession();
        session.setPartNumber(Utils.getCallbackData(callbackQuery));

        setSession(chatId, session);

        send(
            EditMessageText
                .builder()
                .chatId(chatId)
                .text(
                    EmojiParser.parseToUnicode(":gear: Введите количество")
                )
                .messageId(callbackQuery.getMessage().getMessageId())
                .build()
        );
    }

    @Override
    public void handleInput(long chatId, long userId, String message) {
        AddCartFormSession session = (AddCartFormSession) getSession(chatId);

        if (session == null) {
            return;
        }

        if(session.getQuantity() == 0) {
            if(!Utils.isInteger(message) || Integer.parseInt(message) < 1) {
                send(
                    SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(
                            EmojiParser.parseToUnicode(":warning: Значение не может быть меньше 1")
                        )
                        .build()
                );

                send(
                    SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(
                            EmojiParser.parseToUnicode(":gear: Введите количество")
                        )
                        .build()
                );
            } else {
                session.setQuantity(Integer.parseInt(message));
                session.setComplete(true);

                String partNumber = session.getPartNumber();
                PartEntity partEntity = partsRepository.findByNumber(partNumber).orElseThrow();

                CartItemEntity cartItem = cartItemsRepository
                        .findByUserIdAndPart(userId, partEntity)
                        .orElseGet(() -> new CartItemEntity(0, userId, partEntity, 0));
                cartItem.setQuantity(session.getQuantity());

                cartItemsRepository.save(cartItem);

                send(
                    SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(
                            EmojiParser.parseToUnicode(":white_check_mark: Запчасть успешно добавлена в :shopping_cart:")
                        )
                        .replyMarkup(
                            InlineKeyboardMarkup
                                .builder()
                                .keyboard(
                                    List.of(
                                        new InlineKeyboardRow(cartButton.get()),
                                        new InlineKeyboardRow(homeButton.get())
                                    )
                                )
                                .build()
                        )
                        .build()
                );
            }
        }
    }

    @Override
    public boolean isCompleted(long chatId) {
        return getSession(chatId).isComplete();
    }

    @Getter
    @Setter
    private static class AddCartFormSession extends BotFormSession {
        private String partNumber;
        private int quantity;
    }
}

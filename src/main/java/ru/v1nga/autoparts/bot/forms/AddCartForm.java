package ru.v1nga.autoparts.bot.forms;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
    public void start(Chat chat, CallbackQuery callbackQuery) {
        AddCartFormSession session = new AddCartFormSession();
        session.setPartNumber(Utils.getCallbackData(callbackQuery));

        setSession(chat.getId(), session);

        send(
            EditMessageText
                .builder()
                .chatId(chat.getId())
                .text(getCountFieldText())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build()
        );
    }

    @Override
    public void handleInput(Chat chat, User user, String message) {
        AddCartFormSession session = (AddCartFormSession) getSession(chat.getId());

        if (session == null) {
            return;
        }

        if(session.getQuantity() == 0) {
            if(!Utils.isInteger(message) || Integer.parseInt(message) < 1) {
                sendError(chat.getId(), "Значение не может быть меньше 1");
                send(chat.getId(), getCountFieldText());
            } else {
                session.setQuantity(Integer.parseInt(message));
                session.setComplete(true);

                String partNumber = session.getPartNumber();
                PartEntity partEntity = partsRepository.findByNumber(partNumber).orElseThrow();

                CartItemEntity cartItem = cartItemsRepository
                        .findByUserIdAndPart(user.getId(), partEntity)
                        .orElseGet(() -> new CartItemEntity(0, user.getId(), partEntity, 0));
                cartItem.setQuantity(session.getQuantity());

                cartItemsRepository.save(cartItem);

                send(
                    SendMessage
                        .builder()
                        .chatId(chat.getId())
                        .text(
                            EmojiParser.parseToUnicode(":white_check_mark: Запчасть успешно добавлена в :shopping_cart:")
                        )
                        .replyMarkup(
                            InlineKeyboardMarkup
                                .builder()
                                .keyboard(
                                    List.of(
                                        cartButton.getRow(),
                                        homeButton.getRow()
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
    public boolean isCompleted(Chat chat) {
        return getSession(chat.getId()).isComplete();
    }

    private String getCountFieldText () {
        return EmojiParser.parseToUnicode(":gear: Введите количество");
    }

    @Getter
    @Setter
    private static class AddCartFormSession extends BotFormSession {
        private String partNumber;
        private int quantity;
    }
}

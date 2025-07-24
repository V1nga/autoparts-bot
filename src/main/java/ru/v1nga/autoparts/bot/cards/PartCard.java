package ru.v1nga.autoparts.bot.cards;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.core.card.Card;
import ru.v1nga.autoparts.entities.PartEntity;

import java.util.List;

@Component
public class PartCard extends Card<PartEntity> {

    private String getBody(PartEntity partEntity) {
        return EmojiParser.parseToUnicode(
            Utils.composeMultiline(
                ":wrench: Артикул: " + partEntity.getNumber(),
                ":memo: Описание: " + partEntity.getDescription()
            )
        );
    }

    private List<InlineKeyboardRow> getKeyboard(PartEntity partEntity) {
        return List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton
                    .builder()
                    .text(EmojiParser.parseToUnicode(":shopping_cart: Добавить в корзину"))
                    .callbackData("add-to-cart:" + partEntity.getNumber())
                    .build()
            )
        );
    }

    @Override
    public SendMessage compose(long chatId, PartEntity partEntity) {
        return composeMessage(chatId, getBody(partEntity), getKeyboard(partEntity));
    }

    @Override
    public EditMessageText compose(long chatId, int messageId, PartEntity partEntity) {
        return composeMessage(chatId, messageId, getBody(partEntity), getKeyboard(partEntity));
    }
}

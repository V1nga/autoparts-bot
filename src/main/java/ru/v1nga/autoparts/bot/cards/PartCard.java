package ru.v1nga.autoparts.bot.cards;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.v1nga.autoparts.bot.core.card.Card;
import ru.v1nga.autoparts.entities.PartEntity;

import java.util.List;

@Component
public class PartCard extends Card<PartEntity> {

    @Override
    public SendMessage build(long chatId, PartEntity partEntity) {
        String body = EmojiParser.parseToUnicode(
            String.join(
                "\n",
                List.of(
                    ":wrench: Артикул: " + partEntity.getNumber(),
                    ":memo: Описание: " + partEntity.getDescription()
                )
            )
        );
        List<InlineKeyboardRow> keyboard = List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton
                    .builder()
                    .text(EmojiParser.parseToUnicode(":shopping_cart: Добавить в корзину"))
                    .callbackData("add_to_cart")
                    .build()
            )
        );

        return buildMessage(chatId, body, keyboard);
    }
}

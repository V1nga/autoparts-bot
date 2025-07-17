package ru.v1nga.autoparts.bot.menu;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class MainMenu implements Menu {

    @Override
    public SendMessage build(long chatId) {
        String title = EmojiParser.parseToUnicode(":gear: Магазин \"Autoparts\" :gear:");

        return SendMessage
            .builder()
            .chatId(chatId)
                .text(title)
                .replyMarkup(InlineKeyboardMarkup
                    .builder()
                    .keyboard(
                        List.of(
                            new InlineKeyboardRow(
                                InlineKeyboardButton
                                    .builder()
                                    .text(EmojiParser.parseToUnicode(":mag: Поиск запчасти"))
                                    .callbackData("search")
                                    .build()
                            ),
                            new InlineKeyboardRow(
                                InlineKeyboardButton
                                    .builder()
                                    .text(EmojiParser.parseToUnicode(":shopping_cart: Корзина"))
                                    .callbackData("cart")
                                    .build(),

                                InlineKeyboardButton
                                    .builder()
                                    .text(EmojiParser.parseToUnicode(":package: Заказы"))
                                    .callbackData("search:test")
                                    .build()
                            )
                        )
                    )
                    .build())
                .build();
    }
}

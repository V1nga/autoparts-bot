package ru.v1nga.autoparts.bot.buttons;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.v1nga.autoparts.bot.core.button.BotButton;

@Component
public class SearchButton extends BotButton {

    @Override
    public InlineKeyboardButton get() {
        return InlineKeyboardButton
            .builder()
            .text(EmojiParser.parseToUnicode(":mag: Поиск запчасти"))
            .callbackData("search")
            .build();
    }
}

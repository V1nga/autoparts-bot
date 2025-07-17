package ru.v1nga.autoparts.bot.callbacks;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;

@Component
public class SearchCallback extends BotCallback {

    public SearchCallback() {
        super("search");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
//            SendMessage helloMessage = SendMessage
//                .builder()
//                .chatId(chat.getId())
//                .text(EmojiParser.parseToUnicode("Введите артикул запчасти"))
//                .build();
//
//            telegramClient.execute(helloMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

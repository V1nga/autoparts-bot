package ru.v1nga.autoparts.bot.commands;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.buttons.HomeButton;

import java.util.List;

@Component
public class StartCommand extends BotCommand {

    @Autowired
    private HomeButton homeButton;

    public StartCommand() {
        super("start", "Команда для запуска бота");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] strings) {
        try {
            SendMessage helloMessage = SendMessage
                .builder()
                .chatId(chat.getId())
                .text(
                    EmojiParser.parseToUnicode(
                        String.join("\n", List.of(
                            ":wave: Мы рады видеть вас!",
                            "",
                            "Я - бот магазина автозапчастей и помогу вам:",
                            ":wrench: Найти нужную деталь",
                            ":dollar: Узнать цену и оформить заказ"
                        ))
                    )
                )
                .replyMarkup(
                    InlineKeyboardMarkup
                        .builder()
                        .keyboard(List.of(homeButton.getRow()))
                        .build()
                )
                .build();

            telegramClient.execute(helloMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
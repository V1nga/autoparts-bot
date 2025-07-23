package ru.v1nga.autoparts.bot.commands;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.menus.MainMenu;
import ru.v1nga.autoparts.repositories.UsersRepository;

import java.util.List;

@Component
public class StartCommand extends BotCommand {

    private final UsersRepository usersRepository;

    @Autowired
    private HomeButton homeButton;
    @Autowired
    private MainMenu mainMenu;

    public StartCommand(UsersRepository usersRepository) {
        super("start", "Команда для запуска бота");
        this.usersRepository = usersRepository;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] strings) {
        try {
            if(!usersRepository.existsByUserId(user.getId())) {
                SendMessage helloMessage = SendMessage
                    .builder()
                    .chatId(chat.getId())
                    .text(
                        Utils.escapeMarkdownV2(
                            EmojiParser.parseToUnicode(
                                String.join("\n", List.of(
                                    ":wave: Мы рады видеть вас!",
                                    "",
                                    "Я - бот магазина автозапчастей и помогу вам:",
                                    ":wrench: Найти нужную деталь",
                                    ":dollar: Узнать цену и оформить заказ",
                                    "",
                                    ":warning: *Перед началом использования бота* :robot_face:",
                                    "Пожалуйста, укажите ваше *ФИО* :bust_in_silhouette: и *номер телефона* :telephone:",
                                    "Эти данные необходимы для вашей идентификации и обработки заказов :white_check_mark:"
                                ))
                            )
                        )
                    )
                    .parseMode("MarkdownV2")
                    .replyMarkup(
                        InlineKeyboardMarkup
                            .builder()
                            .keyboard(
                                List.of(
                                    new InlineKeyboardRow(
                                        InlineKeyboardButton
                                            .builder()
                                            .text(
                                                EmojiParser.parseToUnicode(":memo: Заполнить информацию о себе")
                                            )
                                            .callbackData("information-fill")
                                            .build()
                                    )
                                )
                            )
                            .build()
                    )
                    .build();

                telegramClient.execute(helloMessage);
            } else {
                telegramClient.execute(mainMenu.build(chat.getId()));
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
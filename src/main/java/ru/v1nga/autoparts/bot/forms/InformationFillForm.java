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
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.core.form.BotForm;
import ru.v1nga.autoparts.bot.core.form.BotFormSession;
import ru.v1nga.autoparts.entities.UserEntity;
import ru.v1nga.autoparts.repositories.UsersRepository;

import java.util.List;

@Component
public class InformationFillForm extends BotForm {

    private final UsersRepository usersRepository;

    @Autowired
    private HomeButton homeButton;

    public InformationFillForm(TelegramClient telegramClient, UsersRepository usersRepository) {
        super("information-fill", telegramClient);
        this.usersRepository = usersRepository;
    }

    @Override
    public void start(Chat chat, CallbackQuery callbackQuery) {
        InformationFillFormSession session = new InformationFillFormSession();
        setSession(chat.getId(), session);

        send(
            EditMessageText
                .builder()
                .chatId(chat.getId())
                .text(getFullNameFieldText())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build()
        );
    }

    @Override
    public void handleInput(Chat chat, User user, String message) {
        InformationFillFormSession session = (InformationFillFormSession) getSession(chat.getId());

        if(session == null) {
            return;
        }

        if(session.getFullName() == null) {
            session.setFullName(message);
            send(chat.getId(), getPhoneFieldText());
        } else if(session.getPhoneNumber() == null) {
            if(!Utils.isValidPhoneNumber(message)) {
                sendError(chat.getId(), "Номер телефона не соответствует формату +7xxxxxxxxxx");
                send(chat.getId(), getPhoneFieldText());
            } else {
                session.setPhoneNumber(message);
                session.setComplete(true);

                usersRepository.save(
                    new UserEntity(0, user.getId(), session.getPhoneNumber(), session.getFullName())
                );

                SendMessage completeMessage = SendMessage
                    .builder()
                    .chatId(chat.getId())
                    .text(
                        EmojiParser.parseToUnicode(
                            Utils.buildMultiline(
                                ":white_check_mark: Форма успешно заполнена!",
                                "Спасибо за предоставленную информацию :blush:",
                                "Вы можете продолжить работу с ботом."
                            )
                        )
                    )
                    .replyMarkup(
                        InlineKeyboardMarkup
                            .builder()
                            .keyboard(List.of(homeButton.getRow()))
                            .build()
                    )
                    .build();
                send(completeMessage);
            }
        }
    }

    @Override
    public boolean isCompleted(Chat chat) {
        return getSession(chat.getId()).isComplete();
    }

    private String getFullNameFieldText() {
        return EmojiParser.parseToUnicode(":bust_in_silhouette: Введите ФИО");
    }

    private String getPhoneFieldText() {
        return EmojiParser.parseToUnicode(":telephone: Введите номер телефона в формате +7xxxxxxxxxx");
    }

    @Getter
    @Setter
    private static class InformationFillFormSession extends BotFormSession {
        private String phoneNumber;
        private String fullName;
    }
}

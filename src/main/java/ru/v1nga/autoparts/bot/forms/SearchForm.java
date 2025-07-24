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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.Utils;
import ru.v1nga.autoparts.bot.buttons.HomeButton;
import ru.v1nga.autoparts.bot.buttons.SearchButton;
import ru.v1nga.autoparts.bot.core.form.BotForm;
import ru.v1nga.autoparts.bot.core.form.BotFormSession;
import ru.v1nga.autoparts.entities.PartEntity;
import ru.v1nga.autoparts.repositories.PartsRepository;

import java.util.List;
import java.util.stream.Stream;

@Component
public class SearchForm extends BotForm {

    private final PartsRepository partsRepository;

    @Autowired
    private HomeButton homeButton;
    @Autowired
    private SearchButton searchButton;

    public SearchForm(TelegramClient telegramClient, PartsRepository partsRepository) {
        super("search", telegramClient);
        this.partsRepository = partsRepository;
    }

    @Override
    public void start(Chat chat, CallbackQuery callbackQuery) {
        SearchFormSession session = new SearchFormSession();
        setSession(chat.getId(), session);

        send(
            EditMessageText
                .builder()
                .chatId(chat.getId())
                .text(getSearchFieldText())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build()
        );
    }

    @Override
    public void handleInput(Chat chat, User user, String message) {
        SearchFormSession session = (SearchFormSession) getSession(chat.getId());

        if (session == null) {
            return;
        }

        if(session.getSearchedPartNumber() == null) {
            session.setSearchedPartNumber(message);
            session.setComplete(true);

            List<PartEntity> parts = partsRepository.searchPart(message);

            if(!parts.isEmpty()) {
                SendMessage foundedMessage = SendMessage
                    .builder()
                    .chatId(chat.getId())
                    .text(
                        EmojiParser.parseToUnicode(
                            String.format(
                                ":white_check_mark: %s %d %s",
                                Utils.pluralize(
                                    parts.size(),
                                    "Найдена",
                                    "Найдено",
                                    "Найдено"
                                ),
                                parts.size(),
                                Utils.pluralize(
                                    parts.size(),
                                    "запчасть",
                                    "запчасти",
                                    "запчастей"
                                )
                            )
                        )
                    )
                    .replyMarkup(
                        InlineKeyboardMarkup
                            .builder()
                            .keyboard(
                                Stream.concat(
                                    parts
                                        .stream()
                                        .map(partEntity ->
                                            new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                    .builder()
                                                    .text(partEntity.getNumber())
                                                    .callbackData("get-part-details:" + partEntity.getNumber())
                                                    .build()
                                            )
                                        ),
                                    Stream.of(
                                        new InlineKeyboardRow(
                                            homeButton.get()
                                        )
                                    )
                                ).toList()
                            )
                            .build()
                    )
                    .build();

                send(foundedMessage);
            } else {
                SendMessage notFoundPart = SendMessage
                    .builder()
                    .chatId(chat.getId())
                    .text(
                        EmojiParser.parseToUnicode(
                            String.format(":no_entry_sign: Запчасть с артикулом \"%s\" не найдена", message)
                        )
                    )
                    .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboard(
                            List.of(
                                searchButton.getRow(),
                                homeButton.getRow()
                            )
                        )
                        .build()
                    )
                    .build();

                send(notFoundPart);
            }
        }
    }

    @Override
    public boolean isCompleted(Chat chat) {
        return getSession(chat.getId()).isComplete();
    }

    private String getSearchFieldText() {
        return EmojiParser.parseToUnicode(":mag: Введите артикул запчасти");
    }

    @Getter
    @Setter
    private static class SearchFormSession extends BotFormSession {
        private String searchedPartNumber;
    }
}

package ru.v1nga.autoparts.bot.forms;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.cards.PartCard;
import ru.v1nga.autoparts.bot.core.form.BotForm;
import ru.v1nga.autoparts.bot.core.form.BotFormSession;
import ru.v1nga.autoparts.entities.PartEntity;
import ru.v1nga.autoparts.repositories.PartsRepository;

import java.util.List;

@Component
public class SearchForm extends BotForm {

    private final PartsRepository partsRepository;
    private final PartCard partCard;

    public SearchForm(TelegramClient telegramClient, PartsRepository partsRepository, PartCard partCard) {
        super("search", telegramClient);
        this.partsRepository = partsRepository;
        this.partCard = partCard;
    }

    @Override
    public void start(long chatId) {
        SearchFormSession session = new SearchFormSession();
        setSession(chatId, session);

        send(chatId, EmojiParser.parseToUnicode(":mag: Введите артикул запчасти"));
    }

    @Override
    public void handleInput(long chatId, String message) {
        SearchFormSession session = (SearchFormSession) getSession(chatId);

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
                    .chatId(chatId)
                    .text(EmojiParser.parseToUnicode(":white_check_mark: Найдено:"))
                    .build();
                send(foundedMessage);

                parts.forEach(partEntity -> send(partCard.build(chatId, partEntity)));
            } else {
                SendMessage notFoundPart = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(
                        EmojiParser.parseToUnicode(
                            String.format(":no_entry_sign: Запчасть с артикулом \"%s\" не найдена", message)
                        )
                    )
                    .build();

                send(notFoundPart);
            }
        }
    }

    @Override
    public boolean isCompleted(long chatId) {
        return getSession(chatId).isComplete();
    }

    @Getter
    @Setter
    private static class SearchFormSession extends BotFormSession {
        private String searchedPartNumber;
    }
}

package ru.v1nga.autoparts.bot.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.v1nga.autoparts.bot.cards.PartCard;
import ru.v1nga.autoparts.bot.core.callback.BotCallback;
import ru.v1nga.autoparts.entities.PartEntity;
import ru.v1nga.autoparts.repositories.PartsRepository;

@Component
public class GetPartDetailsCallback extends BotCallback {

    @Autowired
    private PartsRepository partsRepository;

    @Autowired
    private PartCard partCard;

    public GetPartDetailsCallback() {
        super("get-part-details");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, CallbackQuery callbackQuery) {
        try {
            String partNumber = getCallbackParam(callbackQuery);
            PartEntity partEntity = partsRepository.findByNumber(partNumber).orElseThrow();

            telegramClient.execute(partCard.build(chat.getId(), callbackQuery.getMessage().getMessageId(), partEntity));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

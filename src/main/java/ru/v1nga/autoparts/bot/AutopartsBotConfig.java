package ru.v1nga.autoparts.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class AutopartsBotConfig {

    private final String botToken;

    public AutopartsBotConfig(@Value("${bot.token}") String botToken) {
        this.botToken = botToken;
    }

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botToken);
    }
}

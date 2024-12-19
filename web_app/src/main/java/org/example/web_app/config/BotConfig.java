package org.example.web_app.config;

import org.example.web_app.bot.TonBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TonBot tonBot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(tonBot); // Регистрация бота
            return botsApi;
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register bot", e);
        }
    }
}

package org.example.web_app.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;


@Component
public class TonBot extends TelegramLongPollingBot {
    public TonBot() {
        System.out.println("TonBot is being initialized");
    }
    @Value("${bot.link}")
    private String link;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private void openWebApp(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Запуск веб-приложения:");


// Создаем кнопку для запуска Web App
        KeyboardButton webAppButton = new KeyboardButton("Открыть Web App");
        webAppButton.setWebApp(new WebAppInfo(link)); // URL вашего веб-приложения

// Создаем строку клавиатуры и добавляем в нее кнопку
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(webAppButton);

// Создаем разметку клавиатуры и добавляем в нее строку
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        //for that looking more beautiful
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

// Устанавливаем разметку клавиатуры в сообщение
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            if (message.equals("/start")) {
                openWebApp(message, chatId);
            } else {
                SendMessage sm = new SendMessage();
                sm.setChatId(chatId);
                sm.setText("Для того чтобы начать напишите \"/start\".");
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    System.err.println("Failed to send message");
                    e.printStackTrace();
                }
            }
        }
    }
}

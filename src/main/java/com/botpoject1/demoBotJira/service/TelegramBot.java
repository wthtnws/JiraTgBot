package com.botpoject1.demoBotJira.service;

import com.botpoject1.demoBotJira.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig botConfig){
        this.config=botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/stop":
                    stopCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId,"Sorry, command was not found");
            }
        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = "Hi, " + firstName +", i will work properly!";
        log.info("Replied to user " + firstName);
        sendMessage(chatId, answer);
    }

    private void stopCommandReceived(long chatId, String firstName) {
        String answer = "Bye, " + firstName;
        log.info("Replied to user " + firstName);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void sendRequest(){
        sendMessage(config.getChatId(), "Current time: " + new java.util.Date().toString());
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}

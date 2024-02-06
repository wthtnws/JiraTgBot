package com.botproject1.demoBotJira.service;

import com.botproject1.demoBotJira.config.BotConfig;
import com.botproject1.demoBotJira.entity.BugEntity;
import com.botproject1.demoBotJira.model.Bug;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.botproject1.demoBotJira.model.BugResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    final BugService bugService;

    public TelegramBot(BotConfig botConfig, BugService bugService){
        this.config=botConfig;
        this.bugService = bugService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().toString());
                    break;
                case "/stop":
                    stopCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/request": {
                        try {
                            sendRequest();
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
            log.error("[TG] Error occurred: " + e.getMessage() + "\n"
            + e.getCause() + "\n"
                    + Arrays.toString(e.getStackTrace()) + "\n");
        }
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void sendRequest() throws URISyntaxException {
        sendMessage(config.getChatId(), "request");
        for (Bug bug: bugService.bugAlertList()){
            sendMessage(config.getChatId(), bug.toString());
            sendMessage(config.getGroupId(), bug.toString());
        }
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

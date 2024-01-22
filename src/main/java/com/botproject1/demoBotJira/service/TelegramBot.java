package com.botproject1.demoBotJira.service;

import com.botproject1.demoBotJira.config.BotConfig;
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
                    startCommandReceived(chatId, update.getMessage().getChat().toString());
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
            log.error("[TG] Error occurred: " + e.getMessage() + "\n"
            + e.getCause() + "\n"
                    + Arrays.toString(e.getStackTrace()) + "\n");
        }
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void sendRequest() throws URISyntaxException {
        String message = getOpenBugs();
        BugResponse bugInformation = new Gson().fromJson(message, BugResponse.class);
        sendMessage(config.getChatId(), "Current time: " + new java.util.Date() + bugInformation);
        sendMessage(config.getGroupId(), "Current time: " + new java.util.Date() + bugInformation);
    }

    synchronized String getOpenBugs() throws URISyntaxException {
        final String uri = "https://pira.myhrlink.ru/rest/agile/1.0/epic/none/issue";
        final String maxResults = "maxResults=3";
        final String jql = "jql=project%20%3D%20HRL%20AND%20type%20%3D%20Bug%20AND%20(Sprint%20not%20in%20openSprints()%20OR%20Sprint%20is%20EMPTY)%20AND%20status%20not%20in%20(Done%2C%20Closed%2C%20%22In%20Progress%22%2C%20%22Under%20Review%22%2C%20%22Ready%20for%20Review%22%2C%20%22READY%20FOR%20TESTING%22%2C%20%22In%20Testing%22)%20ORDER%20BY%20updated%20DESC%2C%20%22%D0%9F%D1%80%D0%B8%D0%BE%D1%80%D0%B8%D1%82%D0%B5%D1%82%20%D0%91%D0%B0%D0%B3%D0%B0%22%20ASC";
        final String fields = "fields=priority%2Ckey%2Ccreated%2Cupdated%2Csummary%2Cstatus";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri + '?'
                        + maxResults + '&'
                        + jql + '&'
                        + fields))
                .header("Accept","application/json")
                .header("Authorization", "Bearer NzY5MjE4ODA1NDA5OkGAUPQHR/U0yjBDcH7nPbL2R97q")
                .GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response code: " + response.statusCode());
            log.info("Response body: " + response.body());
            return response.body();
        } catch (Exception e) {
            log.error("[HttpClient] Error occurred: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return ":^(";
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

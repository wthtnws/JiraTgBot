package com.botproject1.demoBotJira.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.key}")
    String botToken;

    @Value("${bot.chat}")
    Long chatId;

    @Value("${bot.groupChat}")
    Long groupId;
}

package com.botproject1.demoBotJira.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
public class BugInfo {
    private String summary;

    private String created;
    private String updated;

    @SerializedName("customfield_10234")
    private Priority priority;
    private Status status;

    @Override
    public String toString() {
        return "\nСтатус: " + getStatus().getName() +
        "\nОписание: " + getSummary() +
        "\nПриоритет бага: " + Optional.ofNullable(getPriority()).orElse(new Priority("Не указано")).getValue() +
        "\nСоздан: " + LocalDateTime.parse(getCreated(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).
                format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) +
        "\nОбновлен: " + LocalDateTime.parse(getUpdated(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).
                format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}

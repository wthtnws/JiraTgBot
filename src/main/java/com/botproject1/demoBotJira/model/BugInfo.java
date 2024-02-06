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
        return "\nStatus: " + getStatus().getName() +
        "\nSummary: " + getSummary() +
        "\nPriority: " + getPriority().getValue() +
        "\nCreated: " + getCreated().toString() +
        "\nUpdated: " + getUpdated().toString();
    }
}

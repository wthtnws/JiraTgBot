package com.botproject1.demoBotJira.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BugInfo {
    private String summary;
    private String created;
    private String updated;

    private Priority priority;
    private Status status;

    @Override
    public String toString() {
        return "\nStatus: " + getStatus().getName() +
        "\nSummary: " + getSummary() +
        "\nPriority: " + getPriority().getName() +
        "\nCreated: " + getCreated() +
        "\nUpdated: " + getUpdated();
    }
}

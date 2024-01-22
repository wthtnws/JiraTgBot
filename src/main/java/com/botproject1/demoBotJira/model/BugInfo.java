package com.botproject1.demoBotJira.model;

import lombok.Getter;

public class BugInfo {
    @Getter
    private String summary;
    @Getter
    private String created;
    @Getter
    private String updated;
    private class Priority {
        private String name;

        public String getName() {
            return name;
        }
    }

    private Priority priority;

    @Override
    public String toString() {
        return "\nSummary: " + getSummary() +
        "\nPriority: " + this.priority.getName() +
        "\nCreated: " + getCreated() +
        "\nUpdated: " + getUpdated();
    }
}

package com.botproject1.demoBotJira.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Bug {
    private String id;
    private String self;
    private String key;
    @SerializedName("fields")
    private BugInfo bugInfo;

    @Override
    public String toString() {
        return getBugInfo().toString() +
                "\nLink: https://pira.myhrlink.ru/browse/" + getKey() +
                "\n";
    }
}

package com.botproject1.demoBotJira.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class BugResponse {
    private String startAt;
    private String maxResults;
    private String total;
    @SerializedName("issues")
    private List<Bug> bugs;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Total: " + getTotal() + '\n');
        for (Bug bug: this.getBugs()){
            stringBuilder.append(bug.toString());
        }
        return stringBuilder.toString();
    }
}

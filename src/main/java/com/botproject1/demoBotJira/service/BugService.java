package com.botproject1.demoBotJira.service;

import com.botproject1.demoBotJira.entity.BugEntity;
import com.botproject1.demoBotJira.mapper.BugMapper;
import com.botproject1.demoBotJira.model.Bug;
import com.botproject1.demoBotJira.model.BugResponse;
import com.botproject1.demoBotJira.repository.BugRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BugService {
    private final BugMapper bugMapper;
    private final BugRepository bugRepository;

    private List<BugEntity> bugEntityList(){
        return bugRepository.findAll();
    }

    private List<Bug> jiraBugList() throws URISyntaxException {
        String response = getOpenBugs();
        BugResponse bugResponse = bugMapper.toDto(response);
        return bugResponse.getBugs();
    }

    @Transactional
    public List<Bug> bugAlertList() throws URISyntaxException {
        List<Bug> jiraBugList = jiraBugList();
        List<BugEntity> newBugs = new ArrayList<>();
        List<Bug> alertList = new ArrayList<>();

        for(Bug bug: jiraBugList){

            Optional<BugEntity> optionalBugEntity = bugRepository.findByJiraBugId(bug.getBugId());

            if (optionalBugEntity.isPresent()){

                BugEntity bugEntity = optionalBugEntity.get();

                if (bug.getBugInfo().getStatus().getName().equals("Готово") &&
                    bugEntity.getStatus().equals("Сделать")){

                    BugEntity updateBugEntity = bugMapper.toEntity(bug);
                    updateBugEntity.setId(bugEntity.getId());

                    newBugs.add(updateBugEntity);

                    alertList.add(bug);
                }
            }
            else
            {
                BugEntity newBugEntity = bugMapper.toEntity(bug);

                newBugs.add(newBugEntity);

                if (newBugEntity.getStatus().equals("Сделать"))
                    alertList.add(bug);
            }

        }

        bugRepository.saveAll(newBugs);

        return alertList;
    }


    private String getOpenBugs() throws URISyntaxException {
        final String uri = "https://deadbeef.deadbeef.deadbeef/rest/agile/1.0/epic/none/issue";
        final String maxResults = "maxResults=20";
        final String jql = "jql=deadbeef";
        final String fields = "fields=deadbeef";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri + '?'
                        + maxResults + '&'
                        + jql + '&'
                        + fields))
                .header("Accept","application/json")
                .header("Authorization", "deadbeef")
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
}

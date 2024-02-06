package com.botproject1.demoBotJira.service;

import com.botproject1.demoBotJira.entity.BugEntity;
import com.botproject1.demoBotJira.mapper.BugMapper;
import com.botproject1.demoBotJira.model.Bug;
import com.botproject1.demoBotJira.repository.BugRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BugService {
    private final BugMapper bugMapper;
    private final BugRepository bugRepository;

    public List<BugEntity> bugEntityList(){
        BugEntity bugEntity = bugRepository.findAll().get(0);
        Bug bug = bugMapper.toModel(bugEntity);
        return bugRepository.findAll();
    }
}

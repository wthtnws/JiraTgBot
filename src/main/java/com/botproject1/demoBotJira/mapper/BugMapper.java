package com.botproject1.demoBotJira.mapper;

import com.botproject1.demoBotJira.entity.BugEntity;
import com.botproject1.demoBotJira.model.*;
import com.google.gson.Gson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface BugMapper {
    default Bug toModel(BugEntity entity){
        Bug bug = new Bug();
        bug.setBugId(entity.getJiraBugId());
        bug.setSelf(null);
        bug.setKey(entity.getKey());

        BugInfo bugInfo = new BugInfo();
        bugInfo.setSummary(entity.getSummary());
//        bugInfo.setCreated(entity.getCreated().toLocalDateTime());
//        bugInfo.setUpdated(entity.getUpdated().toLocalDateTime());
        bugInfo.setCreated(entity.getCreated().toString());
        bugInfo.setUpdated(entity.getUpdated().toString());
        bugInfo.setPriority(new Priority(entity.getPriority()));
        bugInfo.setStatus(new Status(entity.getStatus()));

        bug.setBugInfo(bugInfo);

        return bug;
    }

    @Mapping(source = "bugId", target = "jiraBugId")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "key", target = "link", qualifiedByName = "toLink")
    @Mapping(source = "bugInfo.summary", target = "summary")
    @Mapping(source = "bugInfo.created", target = "created", qualifiedByName = "toOffsetDate")
    @Mapping(source = "bugInfo.updated", target = "updated", qualifiedByName = "toOffsetDate")
    @Mapping(source = "bugInfo.priority.value", target = "priority")
    @Mapping(source = "bugInfo.status.name", target = "status")
    BugEntity toEntity (Bug bug);
    @Named("toLink")
    default String toLink(String key){
        return "https://pira.myhrlink.ru/browse/" + key;
    }
//    @Named("toOffsetDate")
//    default OffsetDateTime toOffsetDate(LocalDateTime time){
//        return OffsetDateTime.of(time, ZoneOffset.ofHours(3));
//    }
    @Named("toOffsetDate")
    default OffsetDateTime toOffsetDate(String time){
        LocalDateTime localDateTime = LocalDateTime.parse(time,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        return OffsetDateTime.of(localDateTime,ZoneOffset.ofHours(0));
    }

    default BugResponse toDto(String response){
        return new Gson().fromJson(response, BugResponse.class);
    }

}

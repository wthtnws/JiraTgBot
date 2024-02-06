package com.botproject1.demoBotJira.mapper;

import com.botproject1.demoBotJira.entity.BugEntity;
import com.botproject1.demoBotJira.model.Bug;
import com.botproject1.demoBotJira.model.BugInfo;
import com.botproject1.demoBotJira.model.Priority;
import com.botproject1.demoBotJira.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface BugMapper {
    default Bug toModel(BugEntity entity){
        Bug bug = new Bug();
        bug.setId(entity.getJiraBugId());
        bug.setSelf(null);
        bug.setKey(entity.getKey());

        BugInfo bugInfo = new BugInfo();
        bugInfo.setSummary(entity.getSummary());
        bugInfo.setCreated(entity.getCreated().toString());
        bugInfo.setUpdated(entity.getUpdated().toString());
        bugInfo.setPriority(new Priority(entity.getPriority()));
        bugInfo.setStatus(new Status(entity.getStatus()));

        bug.setBugInfo(bugInfo);

        return bug;
    }

    @Mapping(source = "id", target = "jiraBugId")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "key", target = "link", qualifiedByName = "toLink")
    @Mapping(source = "bugInfo.summary", target = "summary")
    @Mapping(source = "bugInfo.created", target = "created", qualifiedByName = "toOffsetDate")
    @Mapping(source = "bugInfo.updated", target = "updated", qualifiedByName = "toOffsetDate")
    @Mapping(source = "bugInfo.priority.name", target = "priority")
    @Mapping(source = "bugInfo.status.name", target = "status")
    BugEntity toEntity (Bug bug);
    @Named("toLink")
    default String toLink(String key){
        return "https://pira.myhrlink.ru/browse/" + key;
    }
    @Named("toOffsetDate")
    default OffsetDateTime toOffsetDate(String time){
        return OffsetDateTime.parse(time);
    }

}

package com.botproject1.demoBotJira.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bug")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BugEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "jira_bug_id", nullable = false)
    private String jiraBugId;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "summary")
    private String summary;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime created;

    @Column(name = "updated_date")
    private OffsetDateTime updated;

    @Column(name = "priority")
    private String priority;

    @Column(name = "status")
    private String status;
}

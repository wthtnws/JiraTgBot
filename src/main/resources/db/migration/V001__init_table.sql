create table IF NOT EXISTS bug
(
    id           uuid not null
        constraint bug_pk
            primary key,
    jira_bug_id  text not null
        constraint jira_bug_id_uk
            unique,
    key          text not null
        constraint key_uk
            unique,
    link         text not null,
    summary      text,
    created_date timestamp not null,
    updated_date timestamp,
    priority     text,
    status       text
);

create index IF NOT EXISTS created_date_index
    on bug (created_date desc);


-- begin COMMENTINGDATA_COMMENT
create table COMMENTINGDATA_COMMENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    IN_RESPONSE_TO_ID uuid,
    RELATED_ENTITY_ID uuid not null,
    RELATED_ENTITY_CLASS varchar(255),
    TOPIC varchar(255) not null,
    MESSAGE varchar(255) not null,
    BACKUP_MESSAGE varchar(255),
    BACKUP_TOPIC varchar(255),
    USER_ID uuid,
    FILE_DESC_ID uuid,
    --
    primary key (ID)
)^
-- end COMMENTINGDATA_COMMENT

create table if not exists SchemaMigration
(
    id           text primary key,
    filename     text,
    description  text,
    dateExecuted text
);

create table if not exists bootstrap(
    exist text primary key
)
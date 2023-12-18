create table if not exists SchemaMigration
(
    id           text primary key,
    filename     text,
    date_executed text
);

create table if not exists bootstrap(
    exist text primary key
)
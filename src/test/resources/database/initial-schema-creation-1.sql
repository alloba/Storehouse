create table if not exists Archive
(
    id           text unique primary key,
    date_created text not null,
    date_updated text not null,

    name         text unique,
    description  text
);

create table if not exists Snapshot
(
    id           text unique primary key,
    date_created text not null,
    date_updated text not null,

    description  text,
    archive_id   text not null,
    foreign key (archive_id) references Archive (id)
);

create table if not exists File
(
    id           text unique primary key,
    date_created text        not null,
    date_updated text        not null,

    md5_hash     text unique not null,
    size_bytes   integer     not null
);

create table if not exists FileMeta
(
    id             text unique primary key,
    date_created   text not null,
    date_updated   text not null,

    original_path  text not null,
    name           text not null,
    file_extension text not null,
    file_id        text not null,
    snapshot_id    text not null,

    foreign key (snapshot_id) references Snapshot (id),
    foreign key (file_id) references File (id)
);


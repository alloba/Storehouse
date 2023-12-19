create table if not exists Archive(
    id text unique primary key,
    date_created text,
    date_updated text,

    name text unique,
    description text
);

create table if not exists Snapshot(
    id text unique primary key,
    date_created text,
    date_updated text,

    archive_id text,
    foreign key (archive_id) references Archive(id)
);

create table if not exists FileMeta(
    id text unique primary key,
    date_created text,
    date_updated text,

    snapshot_path text,
    snapshot_id text,
    foreign key (snapshot_id) references Snapshot(id),
    file_id text,
    foreign key (file_id) references File(id)
);

create table if not exists File(
    id text primary key,
    date_created text,
    date_updated text,

    name text,
    file_extension text,
    md5_hash text
);
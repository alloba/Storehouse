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

    archive_id text,
    foreign key (archive_id) references Archive(id)

);

create table if not exists ArchiveFileMeta (
    archive_id text,
    filemeta_id text,

    foreign key (archive_id) references Archive(id),
    foreign key (filemeta_id) references FileMeta(id),
    primary key (archive_id, filemeta_id)
);

create table if not exists SnapshotFileMeta(
    snapshot_id text,
    filemeta_id text,
    foreign key (snapshot_id) references Snapshot(id),
    foreign key (filemeta_id) references FileMeta(id),
    primary key (snapshot_id, filemeta_id)
);
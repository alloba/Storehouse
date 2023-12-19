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

    description text,
    archive_id text,
    foreign key (archive_id) references Archive(id)
);

create table if not exists File(
   id text unique primary key,
   date_created text,
   date_updated text,

   name text,
   file_extension text,
   md5_hash text
);

create table if not exists FileMeta(
    id text unique primary key,
    date_created text,
    date_updated text,

    snapshot_path text,
    file_id text,
    snapshot_id text,

    foreign key (snapshot_id) references Snapshot(id),
    foreign key (file_id) references File(id)
);


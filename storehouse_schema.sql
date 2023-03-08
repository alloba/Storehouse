create table if not exists archive
(
    id           TEXT not null primary key unique,
    name         text not null,
    created_date text not null,
    updated_date text not null
);

create table if not exists snapshot
(
    id           text not null primary key unique,
    name         text not null,
    created_date text not null,
    updated_date text not null
);

create table if not exists collection
(
    id           text not null primary key unique,
    name         text not null,
    created_date text not null,
    updated_date text not null
);

create table if not exists filemeta
(
    id           text not null primary key unique,
    name         text not null,
    extension    text not null,
    size_bytes   int  not null,
    created_date text not null,
    updated_date text not null
);

create table if not exists fileraw
(
    id           text not null primary key unique,
    hash         text not null,
    created_date text not null,
    updated_date text not null
);

-------------------Junctions------------------------------

create table if not exists archive_snapshot
(
    archive_id  text not null,
    snapshot_id text not null,
    primary key (archive_id, snapshot_id),
    foreign key (archive_id) references archive (id),
    foreign key (snapshot_id) references snapshot (id)
);

create table if not exists snapshot_collection
(
    snapshot_id   text not null,
    collection_id text not null,
    primary key (snapshot_id, collection_id),
    foreign key (snapshot_id) references snapshot (id),
    foreign key (collection_id) references collection (id)
);

create table if not exists collection_collection
(
    parent_collection_id text not null,
    child_collection_id  text not null,
    primary key (parent_collection_id, child_collection_id),
    foreign key (parent_collection_id) references collection (id),
    foreign key (child_collection_id) references collection (id)
);

create table if not exists collection_filemeta
(
    collection_id text not null,
    filemeta_id   text not null,
    primary key (collection_id, filemeta_id),
    foreign key (collection_id) references collection (id),
    foreign key (filemeta_id) references filemeta (id)
);

create table if not exists filemeta_fileraw
(
    filemeta_id text not null,
    fileraw_id  text not null,
    primary key (filemeta_id, fileraw_id),
    foreign key (filemeta_id) references filemeta (id),
    foreign key (fileraw_id) references fileraw (id)
);
# Storehouse

Data backup tool.

Hopefully a successor to my Librarian project.

## Development Goals

- store snapshots of data
- ability to keep multiple separate collections
- basic file deduplication to keep overall size down
- file compression to keep size down
- in-place metadata storage via sqlite or similar
- configurable source and destination types, easily swappable.
    - local disk
    - cloud (s3/other)
- ability to see differences between snapshots, and between current and a snapshot
- ability to restore single files and entire archives
- comprehensive history of all actions taken
- metadata for all files within an archive - date collected, date updated, number of versions, etc.
- fully configurable by external file, which can be stored with the archive if desired
- provide an interface that is easy to integrate with an external scheduler (make a nice CLI interface with sensible parameters)
- ability to lock out other instances of the tool to prevent data corruption
    - file source systems provide file-level protections decently enough for my needs
    - just need to prevent accidentally running multiple instances and mixing data 
package reporting.models

import ArchiveOperator
import database.entities.ArchiveEntity

class ArchiveOverviewModel(private val archiveEntity: ArchiveEntity, private val archiveOperator: ArchiveOperator) {

    override fun toString(): String {
        val archiveSnapshots = archiveOperator.getSnapshotsByArchive(archiveEntity)
        val mostRecentSnapshot = archiveSnapshots.maxByOrNull { it.dateCreated }
        val recentSnapshotFileMetas = archiveOperator.getFileMetasBySnapshotId(mostRecentSnapshot?.id ?: "")
        val archiveFiles = archiveOperator.getFilesByArchive(archiveEntity)

        return """
            Archive ${archiveEntity.name}
                - ID: ${archiveEntity.id}
                - Created: ${archiveEntity.dateCreated}
                - Updated: ${archiveEntity.dateUpdated}
                - Snapshot Count: ${archiveSnapshots.size}
                - Last Snapshot Created: ${mostRecentSnapshot?.dateCreated ?: "ERR - Could not sort snapshots"}
                    - Associated File Metadata Count: ${recentSnapshotFileMetas.size}
                - Total Associated Raw Files: ${archiveFiles.size}
                    - Estimated Total Size: ${archiveFiles.sumOf { it.sizeBytes }} Bytes
        """.trimIndent()
    }
}
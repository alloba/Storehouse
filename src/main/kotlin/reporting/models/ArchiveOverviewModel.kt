package reporting.models

import ArchiveManager
import database.entities.ArchiveEntity

class ArchiveOverviewModel(private val archiveEntity: ArchiveEntity, private val archiveManager: ArchiveManager) {

    override fun toString(): String {
        val archiveSnapshots = archiveManager.getSnapshotsByArchive(archiveEntity)
        val mostRecentSnapshot = archiveSnapshots.maxByOrNull { it.dateCreated }
        val recentSnapshotFileMetas = archiveManager.getFileMetasBySnapshotId(mostRecentSnapshot?.id ?: "")
        val archiveFiles = archiveManager.getFilesByArchive(archiveEntity)

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
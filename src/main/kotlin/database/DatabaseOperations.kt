package database

import database.entities.SchemaMigrationEntity
import java.time.format.DateTimeFormatter

fun getSchemaMigrations(database: StorehouseDatabase): List<SchemaMigrationEntity> {
    val query = "select id, filename, date_executed from ${DatabaseConstants.SCHEMA_MIGRATION_TABLE}"
    val rs = database.connection.prepareStatement(query).executeQuery()

    val found = mutableListOf<SchemaMigrationEntity>()
    while (rs.next()){
        found.add(SchemaMigrationEntity.fromResultSet(rs))
    }

    return found.toList()
}

fun insertSchemaMigration(database: StorehouseDatabase, migration: SchemaMigrationEntity): SchemaMigrationEntity {
    val query = "insert into ${DatabaseConstants.SCHEMA_MIGRATION_TABLE} (id, filename, date_executed) values (?,?, ?)"
    val statement = database.connection.prepareStatement(query)
    statement.setString(1, migration.id)
    statement.setString(2, migration.filename)
    statement.setString(3, migration.dateExecuted.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    statement.executeUpdate()
    return migration
}

fun pingBootstrap(database: StorehouseDatabase): Boolean {
    val query = "select 1 from ${DatabaseConstants.BOOTSTRAP_TABLE}"
    database.connection.prepareStatement(query).executeQuery()
    return true
}

class DatabaseConstants {
    companion object {
        const val BOOTSTRAP_TABLE = "bootstrap"
        const val SCHEMA_MIGRATION_TABLE = "SchemaMigration"
    }
}
//private const val BootstrapTable =  "bootstrap"
//private const val SchemaMigrationTable = "SchemaMigration"
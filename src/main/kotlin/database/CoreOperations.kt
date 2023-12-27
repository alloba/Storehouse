package database

import database.entities.SchemaMigrationEntity
import toDb

fun getSchemaMigrations(database: StorehouseDatabase): List<SchemaMigrationEntity> {
    val query = "select id, filename, date_executed from SchemaMigration"
    val rs = database.connection.prepareStatement(query).executeQuery()

    val found = mutableListOf<SchemaMigrationEntity>()
    while (rs.next()) {
        found.add(SchemaMigrationEntity.fromResultSet(rs))
    }

    return found.toList()
}


fun insertSchemaMigration(database: StorehouseDatabase, migration: SchemaMigrationEntity): SchemaMigrationEntity {
    val query = "insert into SchemaMigration (id, filename, date_executed) values (?,?, ?)"
    val statement = database.connection.prepareStatement(query)
    statement.setString(1, migration.id)
    statement.setString(2, migration.filename)
    statement.setString(3, migration.dateExecuted.toDb())
    statement.executeUpdate()
    return migration
}

fun pingBootstrap(database: StorehouseDatabase): Boolean {
    val query = "select 1 from bootstrap"
    database.connection.prepareStatement(query).executeQuery()
    return true
}

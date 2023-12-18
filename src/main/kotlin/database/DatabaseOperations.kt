package database

import database.entities.SchemaMigrationEntity

fun getSchemaMigrations(database: StorehouseDatabase): List<SchemaMigrationEntity> {
    val query = "select id, filename, description, dateExecuted from SchemaMigration"
    val rs = database.connection.prepareStatement(query).executeQuery()

    val found = mutableListOf<SchemaMigrationEntity>()
    while (rs.next()){
        found.add(SchemaMigrationEntity.fromResultSet(rs))
    }

    return found.toList()
}
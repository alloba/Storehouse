package database

import java.sql.Connection
import java.sql.DriverManager


/**
 * Provide the path to the database resource
 * The file is assumed to be a valid sqlite db. This is a hardcoded assumption enforced by the connection string in this function.
 */
fun getSqliteConnection(resourcePath: String): Connection {
    val connectionString = "jdbc:sqlite:$resourcePath"
    val connection = DriverManager.getConnection(connectionString)

    assert(connection.isValid(1))
    if (!connection.isValid(1)){
        throw Exception("invalid connection - " + connection.clientInfo)
    }

    return connection
}

package database

import java.sql.Connection
import java.sql.DriverManager


fun getSqliteConnection(path: String): Connection {
    val connectionString = "jdbc:sqlite:$path"
    val conn = DriverManager.getConnection(connectionString)

    assert(conn.isValid(1))
    return conn ?: throw Exception("invalid connection")
}

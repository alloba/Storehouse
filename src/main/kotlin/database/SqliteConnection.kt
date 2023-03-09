package database

import java.sql.Connection
import java.sql.DriverManager

fun getSqliteConnection(path: String): Connection {
    val connectionString = "jdbc:sqlite:$path"
    try{
        val connection = DriverManager.getConnection(connectionString)
        if (! connection.isValid(10)){
            connection?.close()
            throw Exception("Cannot make connection to sqlite database: $connectionString")
        }
        return connection
    } catch (e: Exception){
        throw Exception("Unable to form database connection", e)
    }
}
package database

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource
import com.j256.ormlite.support.ConnectionSource

fun getSqliteConnection(path: String): ConnectionSource {
    val connectionString = "jdbc:sqlite:$path"
    return JdbcPooledConnectionSource(connectionString)
}

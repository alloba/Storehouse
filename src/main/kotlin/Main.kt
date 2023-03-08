import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

fun main(args: Array<String>) {
    println("Hello World!")

//    val sqlConnection = "jdbc:sqlite:" + "C:\\Users\\alexl\\projects\\Storehouse\\db\\storehouse.sqlite"
//    DriverManager.getConnection(sqlConnection).use { connection ->
//        val statement: Statement = connection.createStatement()
//        statement.queryTimeout = 30
//        statement.executeUpdate("drop table if exists test")
//        statement.executeUpdate("create table test(id integer, name string)")
//        statement.executeUpdate("insert into test values (1, 'hello')")
//        val rs = statement.executeQuery("select * from test")
//
//        while (rs.next()) {
//            println(rs.getInt("id"))
//            println(rs.getString("name"))
//        }
//    }
}

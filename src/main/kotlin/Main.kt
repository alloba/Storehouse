import input.parseArgs


fun main(args: Array<String>) {

    val argsContainer = parseArgs(args)
    println("understood commandline info:")
    println(argsContainer)

//    getSqliteConnection(argsContainer.databaseLocation).use {
//        val dao = DaoAccess(it)
//        CommandType.of(argsContainer.getCommand()).executor(argsContainer, dao)
//    }
}

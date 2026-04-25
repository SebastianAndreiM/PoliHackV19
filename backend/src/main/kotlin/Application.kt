import presentation.plugins.configureAuthentication
import presentation.plugins.configureDI
import presentation.plugins.configureRouting
import presentation.plugins.configureSerialization
import presentation.plugins.configureStatusPages
import io.ktor.server.application.Application
import presentation.plugins.configureDatabases

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureDatabases()
    configureSerialization()
    configureAuthentication()
    configureStatusPages()
    configureRouting()
}
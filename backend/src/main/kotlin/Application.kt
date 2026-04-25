import com.assetguard.presentation.configureAuthentication
import com.assetguard.presentation.configureDI
import com.assetguard.presentation.configureRouting
import com.assetguard.presentation.configureSerialization
import com.assetguard.presentation.configureStatusPages
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
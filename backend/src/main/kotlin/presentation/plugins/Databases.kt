package presentation.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import infra.db.ai.AIChatLogsTable
import infra.db.telemetry.SessionsTable
import infra.db.telemetry.TelemetryEventsTable
import infra.db.user.UsersTable
import io.ktor.server.application.Application
import io.ktor.server.application.log
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val rawDatabaseUrl = System.getenv("DATABASE_URL")
    val resolvedJdbcUrl = when {
        rawDatabaseUrl == null -> environment.config.property("database.url").getString()
        rawDatabaseUrl.startsWith("jdbc:") -> rawDatabaseUrl
        else -> "jdbc:$rawDatabaseUrl"
    }

    val config = HikariConfig().apply {
        jdbcUrl = resolvedJdbcUrl
        username = System.getenv("DATABASE_USER")
            ?: System.getenv("PGUSER")
            ?: environment.config.property("database.user").getString()
        password = System.getenv("DATABASE_PASSWORD")
            ?: System.getenv("PGPASSWORD")
            ?: environment.config.property("database.password").getString()
        driverClassName = environment.config.property("database.driver").getString()
        maximumPoolSize = environment.config.property("database.maxPoolSize")
            .getString().toInt()
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            UsersTable, SessionsTable, TelemetryEventsTable, AIChatLogsTable
        )
    }

    log.info("Database connected and schema verified")
}
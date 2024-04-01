package me.javahere.reachyourgoal

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class PostgresExtension : BeforeAllCallback {
    companion object {
        private var database = PostgreSQLContainer("postgres:16-alpine")
    }

    override fun beforeAll(context: ExtensionContext) {
        if (database.isRunning) return

        database.start()

        System.setProperty("POSTGRES_HOST", database.host)
        System.setProperty("POSTGRES_PORT", database.firstMappedPort.toString())
        System.setProperty("DATABASE_NAME", database.databaseName)
        System.setProperty("POSTGRES_USERNAME", database.username)
        System.setProperty("POSTGRES_PASSWORD", database.password)
    }
}

package me.javahere.reachyourgoal

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class PostgresExtension : BeforeAllCallback, AfterAllCallback {

    private var database = PostgreSQLContainer("postgres:15.3-alpine").withReuse(true)

    override fun afterAll(context: ExtensionContext) {
        database.stop()
    }

    override fun beforeAll(context: ExtensionContext) {
        database.start()

        System.setProperty("POSTGRES_HOST", database.host)
        System.setProperty("POSTGRES_PORT", database.firstMappedPort.toString())
        System.setProperty("DATABASE_NAME", database.databaseName)
        System.setProperty("POSTGRES_USERNAME", database.username)
        System.setProperty("POSTGRES_PASSWORD", database.password)
    }
}

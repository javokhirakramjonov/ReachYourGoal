package me.javahere.reachyourgoal

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(PostgresExtension::class)
abstract class TestContainerRelatedTest
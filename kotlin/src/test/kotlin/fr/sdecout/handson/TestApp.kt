package fr.sdecout.handson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootApplication
class TestApp {

    @Bean
    @ServiceConnection
    fun postgreSQLContainer(): PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15.3")

}

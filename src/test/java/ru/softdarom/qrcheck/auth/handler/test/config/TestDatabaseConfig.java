package ru.softdarom.qrcheck.auth.handler.test.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@TestConfiguration
public class TestDatabaseConfig {

    private static final String POSTGRES_IMAGE = "postgres:12-alpine";
    private static final String INIT_DATABASE_FILE_PATH = "sql/init.sql";

    private static final String COMPATIBLE_POSTGRES = "postgres";

    @Bean(initMethod = "start", destroyMethod = "stop")
    PostgreSQLContainer<?> postgresContainer(DockerImageName dockerPostgres) {
        return new PostgreSQLContainer<>(dockerPostgres)
                .withInitScript(INIT_DATABASE_FILE_PATH)
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    DockerImageName dockerPostgres() {
        return DockerImageName.parse(POSTGRES_IMAGE).asCompatibleSubstituteFor(COMPATIBLE_POSTGRES);
    }

    @Bean
    DataSource dataSource(PostgreSQLContainer<?> postgresContainer) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configureSpyUrl(postgresContainer));
        hikariConfig.setUsername(postgresContainer.getUsername());
        hikariConfig.setPassword(postgresContainer.getPassword());

        return new HikariDataSource(hikariConfig);
    }

    private static String configureSpyUrl(PostgreSQLContainer<?> postgresContainer) {
        return "jdbc:p6spy:postgresql://"
                + postgresContainer.getContainerIpAddress()
                + ":"
                + postgresContainer.getMappedPort(5432)
                + "/"
                + postgresContainer.getDatabaseName();
    }

}

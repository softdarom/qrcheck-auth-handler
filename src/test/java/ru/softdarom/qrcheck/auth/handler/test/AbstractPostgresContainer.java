package ru.softdarom.qrcheck.auth.handler.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractPostgresContainer.PostgreDataSourceInitializer.class)
@Slf4j(topic = "TEST")
public abstract class AbstractPostgresContainer {

    private static final String POSTGRES_IMAGE = "qrcheck/test-postgres:12-alpine";
    private static final String COMPATIBLE_POSTGRES = "postgres";
    private static final String DEFAULT_DATABASE = "qrcheck";
    private static final String DEFAULT_USERNAME = "qrcheck";
    private static final String DEFAULT_PASSWORD = "qrcheck";

    @ClassRule
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = postgreContainerConfigureAndStart();

    private static PostgreSQLContainer<?> postgreContainerConfigureAndStart() {
        var postgreSQLContainer =
                new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE)
                        .asCompatibleSubstituteFor(COMPATIBLE_POSTGRES))
                        .withDatabaseName(DEFAULT_DATABASE)
                        .withUsername(DEFAULT_USERNAME)
                        .withPassword(DEFAULT_PASSWORD);

        postgreSQLContainer.start();
        LOGGER.info("Postgres стартовал");
        LOGGER.info("URL базы данных: {}", postgreSQLContainer.getJdbcUrl());
        LOGGER.info("Username базы данных: {}", postgreSQLContainer.getUsername());
        LOGGER.info("Password базы данных: {}", postgreSQLContainer.getPassword());
        return postgreSQLContainer;
    }

    static class PostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        /**
         * Configuration for spy logging of database
         */
        private static String configureSpyUrl() {
            if (!AbstractPostgresContainer.POSTGRES_CONTAINER.isCreated()) {
                throw new IllegalStateException("A postgres container isn't started!");
            }
            return "jdbc:p6spy:postgresql://" //Note!
                    + AbstractPostgresContainer.POSTGRES_CONTAINER.getContainerIpAddress()
                    + ":"
                    + AbstractPostgresContainer.POSTGRES_CONTAINER.getMappedPort(5432)
                    + "/"
                    + AbstractPostgresContainer.POSTGRES_CONTAINER.getDatabaseName();
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            setupProperties(applicationContext);
        }

        private void setupProperties(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + configureSpyUrl(),
                    "spring.datasource.username=" + AbstractPostgresContainer.POSTGRES_CONTAINER.getUsername(),
                    "spring.datasource.password=" + AbstractPostgresContainer.POSTGRES_CONTAINER.getPassword()
            );
        }
    }

}
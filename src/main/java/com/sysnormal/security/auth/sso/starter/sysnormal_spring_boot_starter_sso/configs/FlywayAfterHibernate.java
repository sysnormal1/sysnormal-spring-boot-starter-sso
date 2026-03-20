package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.configs;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.database.DatabaseProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.spring.SpringProperties;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FlywayAfterHibernate
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.datasource.sso", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({DatabaseProperties.class, SpringProperties.class})
public class FlywayAfterHibernate {

    private static final Logger logger = LoggerFactory.getLogger(FlywayAfterHibernate.class);

    private final DatabaseProperties properties;
    private final SpringProperties springProperties;

    public FlywayAfterHibernate(DatabaseProperties properties, SpringProperties springProperties) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "FlywayAfterHibernate");
        this.properties = properties;
        this.springProperties = springProperties;
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "FlywayAfterHibernate");
    }

    @Bean
    @ConditionalOnMissingBean(name = "ssoFlyway")
    public Flyway ssoFlyway() {
        logger.debug("INIT {}.{} {}", this.getClass().getSimpleName(), "ssoFlyway", springProperties.getFlyway().getLocations());
        Flyway result = null;
        try {
            result = Flyway.configure()
                    .dataSource(
                            properties.getJdbcUrl(),
                            properties.getUsername(),
                            properties.getPassword()
                    )
                    .locations(springProperties.getFlyway().getLocations())
                    .baselineOnMigrate(springProperties.getFlyway().isBaselineOnMigrate())
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "ssoFlyway");
        return result;
    }

    @Bean
    @ConditionalOnMissingBean(name = "ssoRunFlywayAfterHibernate")
    public ApplicationRunner ssoRunFlywayAfterHibernate(Flyway flyway) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "ssoRunFlywayAfterHibernate");
        ApplicationRunner result = null;
        try {
            result = args -> {
                logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "ssoRunFlywayAfterHibernate.ApplicationRunner");
                try {
                    flyway.migrate();
                    logger.debug("Flyway migrations executed after Hibernate initialization.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.debug("END {}.{}", this.getClass().getSimpleName(), "ssoRunFlywayAfterHibernate.ApplicationRunner");
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "ssoRunFlywayAfterHibernate");
        return result;
    }
}
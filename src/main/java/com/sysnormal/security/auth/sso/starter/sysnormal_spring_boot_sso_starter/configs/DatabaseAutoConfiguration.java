package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.configs;

import com.sysnormal.commons.core.utils_core.Constants;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.Agent;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.properties.database.DatabaseProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.properties.spring.SpringProperties;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * DatabaseAutoConfiguration
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
        basePackageClasses = AgentsRepository.class,
        entityManagerFactoryRef = DatabaseAutoConfiguration.entityManagerFactoryQualifier,
        transactionManagerRef = DatabaseAutoConfiguration.transactionManagerQualifier
)
@ConditionalOnProperty(prefix = "spring.datasource.sso", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({DatabaseProperties.class, SpringProperties.class})
public class DatabaseAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseAutoConfiguration.class);

    public static final String datasourceName = "sso";
    public static final String datasourcePrefix = Constants.Spring.DATASOURCE.PROPERTY + "." + datasourceName;
    public static final String persistenceUnitName = datasourceName;
    public static final String entityManagerFactoryQualifier = persistenceUnitName +  Constants.Jakarta.PERSISTENCE.ENTITYMANAGERFACTORY.CLASSNAME;
    public static final String transactionManagerQualifier = persistenceUnitName + Constants.SpringFramework.TRANSACTION.TRANSACTIONMANAGER.CLASSNAME;

    /**
     * properties
     */
    private final DatabaseProperties properties;
    private final SpringProperties springProperties;

    /**
     * default constructor
     * @param properties the properties
     */
    public DatabaseAutoConfiguration(DatabaseProperties properties, SpringProperties springProperties) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "DatabaseAutoConfiguration");
        this.properties = properties;
        this.springProperties = springProperties;
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "DatabaseAutoConfiguration");
    }

    /**
     * datasource
     * @return the datasource
     */
    @Bean
    @Primary
    public DataSource ssoDataSource() {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "ssoDataSource");
        DataSource result = null;
        try {
            result = org.springframework.boot.jdbc.DataSourceBuilder.create()
                    .url(properties.getJdbcUrl())
                    .username(properties.getUsername())
                    .password(properties.getPassword())
                    .driverClassName(properties.getDriverClassName())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "ssoDataSource");
        return result;
    }

    /**
     * entity manager factory
     *
     * @param builder the builder
     * @return the factory bean
     */
    @Bean(name = entityManagerFactoryQualifier)
    @Primary
    @ConditionalOnMissingBean(name = entityManagerFactoryQualifier)
    public LocalContainerEntityManagerFactoryBean ssoEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), entityManagerFactoryQualifier);
        LocalContainerEntityManagerFactoryBean result = null;
        try {
            Map<String, Object> jpaProps = new HashMap<>();
            jpaProps.put(org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO, springProperties.getJpa().getHibernate().getDdlAuto());
            jpaProps.put(Constants.Hibernate.DIALECT.PROPERTY, springProperties.getJpa().getHibernate().getDialect());
            //jpaProps.put("properties", springProperties.getJpa().getProperties());
            jpaProps.put(Constants.Hibernate.GLOBALLY_QUOTED_IDENTIFIERS.PROPERTY, springProperties.getJpa().getHibernate().isGloballyQuotedIdentifiers());
            result = builder
                    .dataSource(ssoDataSource())
                    .packages(
                            Agent.class.getPackageName()
                    )
                    .persistenceUnit(persistenceUnitName)
                    .properties(jpaProps)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), entityManagerFactoryQualifier);
        return result;
    }

    /**
     * transaction manager
     *
     * @param ssoEntityManagerFactory the factory
     * @return the transaction manager
     */
    @Bean(name = transactionManagerQualifier)
    @Primary
    @ConditionalOnMissingBean(name = transactionManagerQualifier)
    public PlatformTransactionManager ssoTransactionManager(@Qualifier(entityManagerFactoryQualifier) EntityManagerFactory ssoEntityManagerFactory) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), transactionManagerQualifier);
        JpaTransactionManager result = null;
        try {
            result = new JpaTransactionManager(ssoEntityManagerFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), transactionManagerQualifier);
        return result;
    }
}
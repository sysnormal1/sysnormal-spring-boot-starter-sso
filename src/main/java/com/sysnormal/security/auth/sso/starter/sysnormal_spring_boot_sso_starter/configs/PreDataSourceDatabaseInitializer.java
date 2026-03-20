package com.sysnormal.security.auth.sso.starter.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Ensures the database exists BEFORE the DataSource is created.
 */
public class PreDataSourceDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(PreDataSourceDatabaseInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "initialize");
        try {
            Environment env = context.getEnvironment();

            String jdbcUrl = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".jdbc-url");
            String rootUsername = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".root-user-name");
            String rootPassword = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".root-password");
            String ddlUserName = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".username"); //ddl-user-name
            String ddlUserPassword = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".password"); //ddl-user-password
            String dmlUserName = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".dml-user-name"); //user with can crud, but not ddl, for user by others services (configure in application.yml)
            String dmlUserPassword = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".dml-user-password");
            String dqlUserName = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".dql-user-name");//user with can only query (select), but not dml or ddl, for user by others services (configure in application.yml)
            String dqlUserPassword = env.getProperty(DatabaseAutoConfiguration.datasourcePrefix + ".dql-user-password");

            logger.debug("environment database variables is url {}, root {}, ddl {}, dml {}", jdbcUrl, rootUsername, ddlUserName, dmlUserName);

            String dialect = detectDialect(jdbcUrl);
            DatabaseConnectionInfo info = extractConnectionInfo(jdbcUrl, dialect, StringUtils.hasText(rootUsername) ? rootUsername : StringUtils.hasText(ddlUserName) ? ddlUserName : dmlUserName);
            if (StringUtils.hasText(rootUsername)) {
                createDatabaseIfNeeded(info, dialect, rootUsername, rootPassword);
                if (StringUtils.hasText(ddlUserName) && !ddlUserName.equalsIgnoreCase("null") && !ddlUserName.equalsIgnoreCase(rootUsername)
                        && !ddlUserName.equalsIgnoreCase("root") && !ddlUserName.equalsIgnoreCase("sysdba") && !ddlUserName.equalsIgnoreCase("sys")
                ) {
                    createUserIfNeeded(info, dialect, ddlUserName, ddlUserPassword, rootUsername, rootPassword);
                    grantDDLPrivileges(info, dialect, ddlUserName, rootUsername, rootPassword);
                }

                if (StringUtils.hasText(dmlUserName) && !dmlUserName.equalsIgnoreCase("null") && !dmlUserName.equalsIgnoreCase(rootUsername)
                        && !dmlUserName.equalsIgnoreCase("root") && !dmlUserName.equalsIgnoreCase("sysdba") && !dmlUserName.equalsIgnoreCase("sys")
                ) {
                    createUserIfNeeded(info, dialect, dmlUserName, dmlUserPassword, rootUsername, rootPassword);
                    grantDMLPrivileges(info, dialect, dmlUserName, rootUsername, rootPassword);
                }

                if (StringUtils.hasText(dqlUserName) && !dqlUserName.equalsIgnoreCase("null") && !dqlUserName.equalsIgnoreCase(rootUsername)
                        && !dqlUserName.equalsIgnoreCase("root") && !dqlUserName.equalsIgnoreCase("sysdba") && !dqlUserName.equalsIgnoreCase("sys")
                ) {
                    createUserIfNeeded(info, dialect, dqlUserName, dqlUserPassword, rootUsername, rootPassword);
                    grantDQLPrivileges(info, dialect, dqlUserName, rootUsername, rootPassword);
                }

            } else if (StringUtils.hasText(ddlUserName) && !ddlUserName.equalsIgnoreCase("null")) {
                //if not has root user and has ddl user, this must have be previous created by dba admin
                createDatabaseIfNeeded(info, dialect, ddlUserName, ddlUserPassword);
                if (StringUtils.hasText(dmlUserName) && !dmlUserName.equalsIgnoreCase("null") && !dmlUserName.equalsIgnoreCase(ddlUserName)
                        && !dmlUserName.equalsIgnoreCase("root") && !dmlUserName.equalsIgnoreCase("sysdba") && !dmlUserName.equalsIgnoreCase("sys")
                ) {
                    createUserIfNeeded(info, dialect, dmlUserName, dmlUserPassword, ddlUserName, ddlUserPassword);
                    grantDMLPrivileges(info, dialect, dmlUserName, ddlUserName, ddlUserPassword);
                }

                if (StringUtils.hasText(dqlUserName) && !dqlUserName.equalsIgnoreCase("null") && !dqlUserName.equalsIgnoreCase(ddlUserName)
                        && !dqlUserName.equalsIgnoreCase("root") && !dqlUserName.equalsIgnoreCase("sysdba") && !dqlUserName.equalsIgnoreCase("sys")
                ) {
                    createUserIfNeeded(info, dialect, dqlUserName, dqlUserPassword, ddlUserName, ddlUserPassword);
                    grantDQLPrivileges(info, dialect, dqlUserName, ddlUserName, ddlUserPassword);
                }
            } else if (StringUtils.hasText(dmlUserName)) {
                //if not has root user and has ddl user, this must have be previous created by dba admin
                createDatabaseIfNeeded(info, dialect, dmlUserName, dmlUserPassword);
            }
        } catch (Exception e) {
            logger.error("Error initializing PreDataSourceDatabaseInitializer", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "initialize");
    }

    private String detectDialect(String url) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "detectDialect");
        String result = null;
        try {
            String lower = url.toLowerCase();
            if (lower.contains("mysql")) result = "mysql";
            if (lower.contains("mariadb")) result = "mariadb";
            if (lower.contains("postgresql")) result = "postgres";
            if (lower.contains("oracle")) result = "oracle";
            if (lower.contains("sqlserver")) result = "mssql";
            if (lower.contains("sqlite")) result = "sqlite";
            if (lower.contains("db2")) result = "db2";
        } catch (Exception e) {
            logger.error("Error detecting dialect", e);
        }
        logger.debug("detected dialect is {}", result);
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "detectDialect");
        return result;
    }

    private DatabaseConnectionInfo extractConnectionInfo(String url, String dialect, String userName) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "extractConnectionInfo");
        DatabaseConnectionInfo result = null;
        try {
            String baseUrl = null, dbName = null;
            switch (dialect) {
                case "mysql":
                case "mariadb":
                case "postgres":
                case "db2":
                    baseUrl = url.substring(0, url.lastIndexOf("/"));
                    dbName = url.substring(url.lastIndexOf("/") + 1).split("\\?")[0];
                    break;
                case "mssql":
                    int idx = url.toLowerCase().indexOf(";databasename=");
                    baseUrl = idx > 0 ? url.substring(0, idx) : url;
                    dbName = idx > 0 ? url.substring(idx + 14).split(";")[0] : null;
                    break;
                case "sqlite":
                    baseUrl = null;
                    dbName = url.replace("jdbc:sqlite:", "");
                    break;
                case "oracle":
                    baseUrl = url;
                    dbName = userName != null ? userName.toUpperCase() : "UNKNOWN";
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported dialect: " + dialect);
            }
            result = new DatabaseConnectionInfo(baseUrl, dbName);
        } catch (Exception e) {
            logger.error("Error extracting connection info", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "extractConnectionInfo");
        return result;
    }

    private void createDatabaseIfNeeded(DatabaseConnectionInfo info, String dialect, String user, String pass) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "createDatabaseIfNeeded");
        try {
            String query = null;
            switch (dialect) {
                case "mysql":
                case "mariadb":
                    query = "CREATE DATABASE IF NOT EXISTS " + info.dbName;
                    execute(info.baseUrl, user, pass, query);
                    break;
                case "postgres":
                    query = "CREATE DATABASE " + info.dbName;
                    execute(info.baseUrl + "/postgres", user, pass, query);
                    break;
                case "mssql":
                    query = "IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'" + info.dbName + "') CREATE DATABASE " + info.dbName;
                    execute(info.baseUrl, user, pass, query);
                    break;
                case "sqlite":
                    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + info.dbName)) {
                        logger.debug("SQLite created/opened: " + info.dbName);
                    }
                    break;
                case "oracle":
                    logger.debug("Oracle: schema assumed to exist for user " + user);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error creating database if needed", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "createDatabaseIfNeeded");
    }

    private void createUserIfNeeded(DatabaseConnectionInfo info, String dialect, String username, String password, String adminUser, String adminPass) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "createUserIfNeeded");
        if (!StringUtils.hasText(username)) return;
        try {
            String sql = null;
            switch (dialect) {
                case "mysql":
                case "mariadb":
                    sql = "CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'";
                    break;
                case "postgres":
                    sql = "DO $$ BEGIN IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '" + username + "') THEN CREATE ROLE " + username + " LOGIN PASSWORD '" + password + "'; END IF; END $$;";
                    break;
                case "mssql":
                    sql = "IF NOT EXISTS (SELECT * FROM sys.sql_logins WHERE name = '" + username + "') BEGIN CREATE LOGIN " + username + " WITH PASSWORD = '" + password + "'; END";
                    break;
                case "oracle":
                    sql = "BEGIN EXECUTE IMMEDIATE 'CREATE USER " + username + " IDENTIFIED BY " + password + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01920 THEN RAISE; END IF; END;";
                    break;
                case "sqlite":
                    logger.debug("SQLite: skipping user creation (not supported)");
                    return;
                case "db2":
                    logger.debug("DB2: user creation handled externally");
                    return;
            }
            execute(info.baseUrl, adminUser, adminPass, sql);
        } catch (Exception e) {
            logger.error("Error creating user", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "createUserIfNeeded");
    }

    private void grantDDLPrivileges(DatabaseConnectionInfo info, String dialect, String username, String adminUser, String adminPass) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "grantDDLPrivileges");
        try {
            String sql = null;
            switch (dialect) {
                case "mysql":
                case "mariadb":
                    sql = "GRANT ALL PRIVILEGES ON " + info.dbName + ".* TO '" + username + "'@'localhost'";
                    break;
                case "postgres":
                    sql = "GRANT ALL PRIVILEGES ON DATABASE " + info.dbName + " TO " + username;
                    break;
                case "mssql":
                    sql = "ALTER ROLE db_owner ADD MEMBER " + username;
                    break;
                case "oracle":
                    sql = "GRANT CONNECT, RESOURCE, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE, CREATE TRIGGER TO " + username;
                    break;
                case "sqlite":
                    logger.debug("SQLite: skipping privileges grant (not supported)");
                    return;
                case "db2":
                    sql = "GRANT DBADM ON DATABASE TO USER " + username;
                    break;
            }
            execute(info.baseUrl + "/" + info.dbName, adminUser, adminPass, sql);
        } catch (Exception e) {
            logger.error("Error granting DDL privileges", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "grantDDLPrivileges");
    }

    private void grantDMLPrivileges(DatabaseConnectionInfo info, String dialect, String username, String adminUser, String adminPass) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "grantDMLPrivileges");
        try {
            String sql = null;
            switch (dialect) {
                case "mysql":
                case "mariadb":
                    sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON " + info.dbName + ".* TO '" + username + "'@'localhost'";
                    break;
                case "postgres":
                    sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO " + username;
                    break;
                case "mssql":
                    sql = "EXEC sp_addrolemember 'db_datareader', '" + username + "'; EXEC sp_addrolemember 'db_datawriter', '" + username + "'";
                    break;
                case "oracle":
                    sql = "GRANT CONNECT, SELECT ANY TABLE, INSERT ANY TABLE, UPDATE ANY TABLE, DELETE ANY TABLE TO " + username;
                    break;
                case "sqlite":
                    logger.debug("SQLite: skipping privileges grant (not supported)");
                    return;
                case "db2":
                    sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON DATABASE TO USER " + username;
                    break;
            }
            execute(info.baseUrl + "/" + info.dbName, adminUser, adminPass, sql);
        } catch (Exception e) {
            logger.error("Error granting DML privileges", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "grantDMLPrivileges");
    }

    private void grantDQLPrivileges(DatabaseConnectionInfo info, String dialect, String username, String adminUser, String adminPass) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "grantDMLPrivileges");
        try {
            String sql = null;
            switch (dialect) {
                case "mysql":
                case "mariadb":
                    sql = "GRANT SELECT ON " + info.dbName + ".* TO '" + username + "'@'localhost'";
                    break;
                case "postgres":
                    sql = "GRANT SELECT ON ALL TABLES IN SCHEMA public TO " + username;
                    break;
                case "mssql":
                    sql = "EXEC sp_addrolemember 'db_datareader', '" + username + "';";
                    break;
                case "oracle":
                    sql = "GRANT CONNECT, SELECT ANY TABLE TO " + username;
                    break;
                case "sqlite":
                    logger.debug("SQLite: skipping privileges grant (not supported)");
                    return;
                case "db2":
                    sql = "GRANT SELECT ON DATABASE TO USER " + username;
                    break;
            }
            execute(info.baseUrl + "/" + info.dbName, adminUser, adminPass, sql);
        } catch (Exception e) {
            logger.error("Error granting DML privileges", e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "grantDMLPrivileges");
    }

    private void execute(String url, String user, String pass, String sql) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "execute");
        logger.debug("getting connection with url {}, user {}, pass {}", url,  user, pass);
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            logger.debug("Executing SQL: {}", sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("exists")) {
                logger.debug("Object already exists: {}", e.getMessage());
            } else {
                logger.error("Error executing SQL", e);
            }
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "execute");
    }

    private record DatabaseConnectionInfo(String baseUrl, String dbName) {}
}

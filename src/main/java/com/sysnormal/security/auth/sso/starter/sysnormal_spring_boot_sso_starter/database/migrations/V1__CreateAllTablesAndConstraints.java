package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.migrations;

import com.sysnormal.commons.spring.spring_data_utils.migration.mysql.MySqlMigrationCreateAllTablesAndConstraints;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.Agent;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class V1__CreateAllTablesAndConstraints extends BaseJavaMigration {

    private static final Logger logger = LoggerFactory.getLogger(V1__CreateAllTablesAndConstraints.class);

    @Override
    public void migrate(Context context) throws SQLException {
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(), "migrate");
        Connection conn = context.getConnection();
        DatabaseMetaData metaData = conn.getMetaData();
        String dbProduct = metaData.getDatabaseProductName().toLowerCase();
        logger.info("Detected database: {}", dbProduct);
        if (dbProduct.contains("mysql") || dbProduct.contains("mariadb")) {
            MySqlMigrationCreateAllTablesAndConstraints.migrate(conn, new String[]{Agent.class.getPackage().getName()});
        } else if (dbProduct.contains("postgresql")) {
            throw new UnsupportedOperationException("Unsupported database vendor: " + dbProduct);
        } else if (dbProduct.contains("oracle")) {
            throw new UnsupportedOperationException("Unsupported database vendor: " + dbProduct);
        } else if (dbProduct.contains("sql server") || dbProduct.contains("microsoft")) {
            throw new UnsupportedOperationException("Unsupported database vendor: " + dbProduct);
        } else {
            throw new UnsupportedOperationException("Unsupported database vendor: " + dbProduct);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(), "migrate");
    }
}
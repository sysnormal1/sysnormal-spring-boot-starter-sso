package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.migrations;

import com.sysnormal.commons.spring.spring_data_utils.JpaReflectionUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Resource;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Adds resources.is_restricted to a database created before the column existed.
 *
 * Conditional on purpose: V1 builds the tables from the entities, so on a database created from now on the
 * column is already there when this runs, and an unconditional ALTER would fail the migration.
 *
 * The default 1 is what keeps this migration from changing anyone's access: every existing resource was
 * already treated as restricted.
 */
public class V3__AddResourcesIsRestricted extends BaseJavaMigration {

    private static final Logger logger = LoggerFactory.getLogger(V3__AddResourcesIsRestricted.class);

    @Override
    public void migrate(Context context) throws SQLException {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "migrate");
        Connection conn = context.getConnection();
        String dbProduct = conn.getMetaData().getDatabaseProductName().toLowerCase();
        if (!(dbProduct.contains("mysql") || dbProduct.contains("mariadb"))) {
            throw new UnsupportedOperationException("Unsupported database vendor: " + dbProduct);
        }

        String tableName = JpaReflectionUtils.resolveTableName(Resource.class);
        boolean exists;
        try (PreparedStatement ps = conn.prepareStatement(
                "select count(1) from information_schema.columns where table_schema = database() and table_name = ? and column_name = 'is_restricted'")) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                exists = rs.getInt(1) > 0;
            }
        }

        if (exists) {
            logger.info("{}.is_restricted already exists, nothing to do", tableName);
        } else {
            String query = "alter table " + tableName
                    + " add column is_restricted tinyint not null default 1,"
                    + " add constraint " + tableName + "_chk_is_restricted check (is_restricted in (0,1))";
            logger.debug("Executing query: {}", query);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(query);
            }
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "migrate");
    }
}

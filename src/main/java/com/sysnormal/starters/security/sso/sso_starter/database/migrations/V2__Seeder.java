package com.sysnormal.starters.security.sso.sso_starter.database.migrations;

import com.sysnormal.libs.utils.database.JpaReflectionUtils;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.*;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.System;
import org.apache.tomcat.util.buf.StringUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class V2__Seeder extends BaseJavaMigration {
    private static final Logger logger = LoggerFactory.getLogger(V2__Seeder.class);
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        try {
            String query = "SET FOREIGN_KEY_CHECKS = 0";
            logger.debug("Executing query: {}", query);
            try (Statement stmt = context.getConnection().createStatement()) {
                stmt.execute(query);
            }

            //AGENTS
            String[] fieldsNames = new String[]{"id", "is_sys_rec", "identifier_type_id","identifier", "email", "password"};
            String[] valuesBinders = new String[]{"?", "?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(Agent.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);

            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Agent.SYSTEM_ID);
                ps.setByte(2, Agent.SYSTEM.getIsSysRec());
                ps.setLong(3, Agent.SYSTEM.getIdentifierTypeId());
                ps.setString(4, Agent.SYSTEM.getIdentifier());
                ps.setString(5, Agent.SYSTEM.getEmail());
                ps.setString(6, Agent.SYSTEM.getPassword());
                ps.executeUpdate();
            }

            //RECORD_STATUS
            fieldsNames = new String[]{"id", "is_sys_rec", "name", "is_active"};
            valuesBinders = new String[]{"?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(RecordStatus.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, RecordStatus.ACTIVE_ID);
                ps.setByte(2, RecordStatus.ACTIVE.getIsSysRec());
                ps.setString(3, RecordStatus.ACTIVE.getName());
                ps.setByte(4, RecordStatus.ACTIVE.getIsActive());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, RecordStatus.INACTIVE_ID);
                ps.setByte(2, RecordStatus.INACTIVE.getIsSysRec());
                ps.setString(3, RecordStatus.INACTIVE.getName());
                ps.setByte(4, RecordStatus.INACTIVE.getIsActive());
                ps.executeUpdate();
            }



            //IDENTIFIER_TYPES
            fieldsNames = new String[]{"id", "is_sys_rec", "name"};
            valuesBinders = new String[]{"?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(IdentifierType.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, IdentifierType.IDENTIFIER_ID);
                ps.setByte(2, IdentifierType.IDENTIFIER.getIsSysRec());
                ps.setString(3, IdentifierType.IDENTIFIER.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, IdentifierType.CODE_ID);
                ps.setByte(2, IdentifierType.CODE.getIsSysRec());
                ps.setString(3, IdentifierType.CODE.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, IdentifierType.CNPJ_ID);
                ps.setByte(2, IdentifierType.CNPJ.getIsSysRec());
                ps.setString(3, IdentifierType.CNPJ.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, IdentifierType.CPF_ID);
                ps.setByte(2, IdentifierType.CPF.getIsSysRec());
                ps.setString(3, IdentifierType.CPF.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, IdentifierType.EMAIL_ID);
                ps.setByte(2, IdentifierType.EMAIL.getIsSysRec());
                ps.setString(3, IdentifierType.EMAIL.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, IdentifierType.PLATE_ID);
                ps.setByte(2, IdentifierType.PLATE.getIsSysRec());
                ps.setString(3, IdentifierType.PLATE.getName());
                ps.executeUpdate();
            }


            //SYSTEM_PLATFORMS
            fieldsNames = new String[]{"id", "is_sys_rec", "name", "is_desktop", "is_web", "is_mobile"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(SystemPlatform.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, SystemPlatform.DESKTOP_ID);
                ps.setByte(2, SystemPlatform.DESKTOP.getIsSysRec());
                ps.setString(3, SystemPlatform.DESKTOP.getName());
                ps.setByte(4, SystemPlatform.DESKTOP.getIsDesktop());
                ps.setByte(5, SystemPlatform.DESKTOP.getIsWeb());
                ps.setByte(6, SystemPlatform.DESKTOP.getIsMobile());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, SystemPlatform.WEB_ID);
                ps.setByte(2, SystemPlatform.WEB.getIsSysRec());
                ps.setString(3, SystemPlatform.WEB.getName());
                ps.setByte(4, SystemPlatform.WEB.getIsDesktop());
                ps.setByte(5, SystemPlatform.WEB.getIsWeb());
                ps.setByte(6, SystemPlatform.WEB.getIsMobile());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, SystemPlatform.MOBILE_ID);
                ps.setByte(2, SystemPlatform.MOBILE.getIsSysRec());
                ps.setString(3, SystemPlatform.MOBILE.getName());
                ps.setByte(4, SystemPlatform.MOBILE.getIsDesktop());
                ps.setByte(5, SystemPlatform.MOBILE.getIsWeb());
                ps.setByte(6, SystemPlatform.MOBILE.getIsMobile());
                ps.executeUpdate();
            }


            //SYSTEM_SIDES
            fieldsNames = new String[]{"id", "is_sys_rec", "name", "is_server", "is_client"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(SystemSide.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, SystemSide.SERVER_SIDE_ID);
                ps.setByte(2, SystemSide.SERVER_SIDE.getIsSysRec());
                ps.setString(3, SystemSide.SERVER_SIDE.getName());
                ps.setByte(4, SystemSide.SERVER_SIDE.getIsServer());
                ps.setByte(5, SystemSide.SERVER_SIDE.getIsClient());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, SystemSide.CLIENT_SIDE_ID);
                ps.setByte(2, SystemSide.CLIENT_SIDE.getIsSysRec());
                ps.setString(3, SystemSide.CLIENT_SIDE.getName());
                ps.setByte(4, SystemSide.CLIENT_SIDE.getIsServer());
                ps.setByte(5, SystemSide.CLIENT_SIDE.getIsClient());
                ps.executeUpdate();
            }

            //SYSTEMS
            fieldsNames = new String[]{"id", "is_sys_rec", "system_platform_id", "system_side_id", "name"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(System.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, System.SSO_SERVER_ID);
                ps.setByte(2, System.SSO_SERVER.getIsSysRec());
                ps.setLong(3, System.SSO_SERVER.getSystemPlatformId());
                ps.setLong(4, System.SSO_SERVER.getSystemSideId());
                ps.setString(5, System.SSO_SERVER.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, System.SSO_WEBCLIENT_ID);
                ps.setByte(2, System.SSO_WEBCLIENT.getIsSysRec());
                ps.setLong(3, System.SSO_WEBCLIENT.getSystemPlatformId());
                ps.setLong(4, System.SSO_WEBCLIENT.getSystemSideId());
                ps.setString(5, System.SSO_WEBCLIENT.getName());
                ps.executeUpdate();
            }


            //RESOURCE_TYPES
            fieldsNames = new String[]{"id", "is_sys_rec", "name"};
            valuesBinders = new String[]{"?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(ResourceType.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.SYSTEM_ID);
                ps.setByte(2, ResourceType.SYSTEM.getIsSysRec());
                ps.setString(3, ResourceType.SYSTEM.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.DATA_ID);
                ps.setByte(2, ResourceType.DATA.getIsSysRec());
                ps.setString(3, ResourceType.DATA.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.TABLE_RESOURCE_ID);
                ps.setByte(2, ResourceType.TABLE.getIsSysRec());
                ps.setString(3, ResourceType.TABLE.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.COLUMN_ID);
                ps.setByte(2, ResourceType.COLUMN.getIsSysRec());
                ps.setString(3, ResourceType.COLUMN.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.MODULE_ID);
                ps.setByte(2, ResourceType.MODULE.getIsSysRec());
                ps.setString(3, ResourceType.MODULE.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.PACKAGE_ID);
                ps.setByte(2, ResourceType.PACKAGE.getIsSysRec());
                ps.setString(3, ResourceType.PACKAGE.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.CLASS_ID);
                ps.setByte(2, ResourceType.CLASS.getIsSysRec());
                ps.setString(3, ResourceType.CLASS.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.METHOD_ID);
                ps.setByte(2, ResourceType.METHOD.getIsSysRec());
                ps.setString(3, ResourceType.METHOD.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.ENDPOINT_ID);
                ps.setByte(2, ResourceType.ENDPOINT.getIsSysRec());
                ps.setString(3, ResourceType.ENDPOINT.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.URL_ID);
                ps.setByte(2, ResourceType.URL.getIsSysRec());
                ps.setString(3, ResourceType.URL.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.SCREEN_ID);
                ps.setByte(2, ResourceType.SCREEN.getIsSysRec());
                ps.setString(3, ResourceType.SCREEN.getName());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.COMPONENT_ID);
                ps.setByte(2, ResourceType.COMPONENT.getIsSysRec());
                ps.setString(3, ResourceType.COMPONENT.getName());
                ps.executeUpdate();
            }

            //RESOURCES
            fieldsNames = new String[]{"id", "is_sys_rec", "parent_id", "system_id", "resource_type_id", "name", "resource_path", "icon", "numeric_order"};
            valuesBinders = new String[]{"?", "?", "?","?", "?", "?","?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(Resource.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.SYSTEMS_ID);
                ps.setByte(2, Resource.SYSTEMS.getIsSysRec());
                ps.setObject(3, Resource.SYSTEMS.getParentId());
                ps.setLong(4, Resource.SYSTEMS.getSystemId());
                ps.setLong(5, Resource.SYSTEMS.getResourceTypeId());
                ps.setString(6, Resource.SYSTEMS.getName());
                ps.setString(7, Resource.SYSTEMS.getResourcePath());
                ps.setString(8, Resource.SYSTEMS.getIcon());
                ps.setInt(9, Resource.SYSTEMS.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.ACCESS_PROFILES_ID);
                ps.setByte(2, Resource.ACCESS_PROFILES.getIsSysRec());
                ps.setObject(3, Resource.ACCESS_PROFILES.getParentId());
                ps.setLong(4, Resource.ACCESS_PROFILES.getSystemId());
                ps.setLong(5, Resource.ACCESS_PROFILES.getResourceTypeId());
                ps.setString(6, Resource.ACCESS_PROFILES.getName());
                ps.setString(7, Resource.ACCESS_PROFILES.getResourcePath());
                ps.setString(8, Resource.ACCESS_PROFILES.getIcon());
                ps.setInt(9, Resource.ACCESS_PROFILES.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.AGENTS_ID);
                ps.setByte(2, Resource.AGENTS.getIsSysRec());
                ps.setObject(3, Resource.AGENTS.getParentId());
                ps.setLong(4, Resource.AGENTS.getSystemId());
                ps.setLong(5, Resource.AGENTS.getResourceTypeId());
                ps.setString(6, Resource.AGENTS.getName());
                ps.setString(7, Resource.AGENTS.getResourcePath());
                ps.setString(8, Resource.AGENTS.getIcon());
                ps.setInt(9, Resource.AGENTS.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.RESOURCES_ID);
                ps.setByte(2, Resource.RESOURCES.getIsSysRec());
                ps.setObject(3, Resource.RESOURCES.getParentId());
                ps.setLong(4, Resource.RESOURCES.getSystemId());
                ps.setLong(5, Resource.RESOURCES.getResourceTypeId());
                ps.setString(6, Resource.RESOURCES.getName());
                ps.setString(7, Resource.RESOURCES.getResourcePath());
                ps.setString(8, Resource.RESOURCES.getIcon());
                ps.setInt(9, Resource.RESOURCES.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.RESOURCE_PERMISSIONS_ID);
                ps.setByte(2, Resource.RESOURCE_PERMISSIONS.getIsSysRec());
                ps.setObject(3, Resource.RESOURCE_PERMISSIONS.getParentId());
                ps.setLong(4, Resource.RESOURCE_PERMISSIONS.getSystemId());
                ps.setLong(5, Resource.RESOURCE_PERMISSIONS.getResourceTypeId());
                ps.setString(6, Resource.RESOURCE_PERMISSIONS.getName());
                ps.setString(7, Resource.RESOURCE_PERMISSIONS.getResourcePath());
                ps.setString(8, Resource.RESOURCE_PERMISSIONS.getIcon());
                ps.setInt(9, Resource.RESOURCE_PERMISSIONS.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.RELATIONSHIPS_ID);
                ps.setByte(2, Resource.RELATIONSHIPS.getIsSysRec());
                ps.setObject(3, Resource.RELATIONSHIPS.getParentId());
                ps.setLong(4, Resource.RELATIONSHIPS.getSystemId());
                ps.setLong(5, Resource.RELATIONSHIPS.getResourceTypeId());
                ps.setString(6, Resource.RELATIONSHIPS.getName());
                ps.setString(7, Resource.RELATIONSHIPS.getResourcePath());
                ps.setString(8, Resource.RELATIONSHIPS.getIcon());
                ps.setInt(9, Resource.RELATIONSHIPS.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.SYSTEMS_X_ACCESS_PROFILES_ID);
                ps.setByte(2, Resource.SYSTEMS_X_ACCESS_PROFILES.getIsSysRec());
                ps.setObject(3, Resource.SYSTEMS_X_ACCESS_PROFILES.getParentId());
                ps.setLong(4, Resource.SYSTEMS_X_ACCESS_PROFILES.getSystemId());
                ps.setLong(5, Resource.SYSTEMS_X_ACCESS_PROFILES.getResourceTypeId());
                ps.setString(6, Resource.SYSTEMS_X_ACCESS_PROFILES.getName());
                ps.setString(7, Resource.SYSTEMS_X_ACCESS_PROFILES.getResourcePath());
                ps.setString(8, Resource.SYSTEMS_X_ACCESS_PROFILES.getIcon());
                ps.setInt(9, Resource.SYSTEMS_X_ACCESS_PROFILES.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.AGENTS_X_ACCESS_PROFILES_ID);
                ps.setByte(2, Resource.AGENTS_X_ACCESS_PROFILES.getIsSysRec());
                ps.setObject(3, Resource.AGENTS_X_ACCESS_PROFILES.getParentId());
                ps.setLong(4, Resource.AGENTS_X_ACCESS_PROFILES.getSystemId());
                ps.setLong(5, Resource.AGENTS_X_ACCESS_PROFILES.getResourceTypeId());
                ps.setString(6, Resource.AGENTS_X_ACCESS_PROFILES.getName());
                ps.setString(7, Resource.AGENTS_X_ACCESS_PROFILES.getResourcePath());
                ps.setString(8, Resource.AGENTS_X_ACCESS_PROFILES.getIcon());
                ps.setInt(9, Resource.AGENTS_X_ACCESS_PROFILES.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.AGENTS_X_SYSTEMS_ID);
                ps.setByte(2, Resource.AGENTS_X_SYSTEMS.getIsSysRec());
                ps.setObject(3, Resource.AGENTS_X_SYSTEMS.getParentId());
                ps.setLong(4, Resource.AGENTS_X_SYSTEMS.getSystemId());
                ps.setLong(5, Resource.AGENTS_X_SYSTEMS.getResourceTypeId());
                ps.setString(6, Resource.AGENTS_X_SYSTEMS.getName());
                ps.setString(7, Resource.AGENTS_X_SYSTEMS.getResourcePath());
                ps.setString(8, Resource.AGENTS_X_SYSTEMS.getIcon());
                ps.setInt(9, Resource.AGENTS_X_SYSTEMS.getNumericOrder());
                ps.executeUpdate();
            }


            //ACCESS_PROFILES
            fieldsNames = new String[]{"id", "is_sys_rec", "name"};
            valuesBinders = new String[]{"?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(AccessProfile.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, AccessProfile.SYSTEM_ID);
                ps.setByte(2, AccessProfile.SYSTEM.getIsSysRec());
                ps.setString(3, AccessProfile.SYSTEM.getName());
                ps.executeUpdate();
            }

            //AGENTS_X_ACCESS_PROFILES
            fieldsNames = new String[]{"is_sys_rec", "agent_id", "access_profile_id"};
            valuesBinders = new String[]{"?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(AgentXAccessProfile.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte)1);
                ps.setLong(2, Agent.SYSTEM_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.executeUpdate();
            }

            //ACCESS_PROFILES_X_SYSTEMS
            fieldsNames = new String[]{"is_sys_rec", "access_profile_id", "system_id"};
            valuesBinders = new String[]{"?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(AccessProfileXSystem.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte)1);
                ps.setLong(2, AccessProfile.SYSTEM_ID);
                ps.setLong(3, System.SSO_WEBCLIENT_ID);
                ps.executeUpdate();
            }

            //RESOURCE_PERMISSIONS
            fieldsNames = new String[]{"is_sys_rec", "resource_id", "access_profile_id, allowed_access, allowed_view, allowed_create, allowed_change, allowed_delete"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(ResourcePermission.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            /*try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte) 1);
                ps.setLong(2, Resource.SYSTEMS_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte) 1);
                ps.setLong(2, Resource.ACCESS_PROFILES_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte) 1);
                ps.setLong(2, Resource.AGENTS_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte) 1);
                ps.setLong(2, Resource.RESOURCES_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.executeUpdate();
            }*/
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte) 1);
                ps.setLong(2, Resource.RESOURCE_PERMISSIONS_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.setByte(4,(byte) 1);
                ps.setByte(5,(byte) 1);
                ps.setByte(6,(byte) 0);
                ps.setByte(7,(byte) 1);
                ps.setByte(8,(byte) 0);
                ps.executeUpdate();
            }

            query = "SET FOREIGN_KEY_CHECKS = 1";
            logger.debug("Executing query: {}", query);
            try (Statement stmt = context.getConnection().createStatement()) {
                stmt.execute(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

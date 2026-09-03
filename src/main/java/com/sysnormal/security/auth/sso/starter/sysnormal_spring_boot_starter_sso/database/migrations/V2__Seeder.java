package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.migrations;

import com.sysnormal.commons.spring.spring_data_utils.JpaReflectionUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.*;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Domain;
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


            //DOMAIN_PLATFORMS
            fieldsNames = new String[]{"id", "is_sys_rec", "name", "is_desktop", "is_web", "is_mobile"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(DomainPlatformType.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, DomainPlatformType.DESKTOP_ID);
                ps.setByte(2, DomainPlatformType.DESKTOP.getIsSysRec());
                ps.setString(3, DomainPlatformType.DESKTOP.getName());
                ps.setByte(4, DomainPlatformType.DESKTOP.getIsDesktop());
                ps.setByte(5, DomainPlatformType.DESKTOP.getIsWeb());
                ps.setByte(6, DomainPlatformType.DESKTOP.getIsMobile());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, DomainPlatformType.WEB_ID);
                ps.setByte(2, DomainPlatformType.WEB.getIsSysRec());
                ps.setString(3, DomainPlatformType.WEB.getName());
                ps.setByte(4, DomainPlatformType.WEB.getIsDesktop());
                ps.setByte(5, DomainPlatformType.WEB.getIsWeb());
                ps.setByte(6, DomainPlatformType.WEB.getIsMobile());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, DomainPlatformType.MOBILE_ID);
                ps.setByte(2, DomainPlatformType.MOBILE.getIsSysRec());
                ps.setString(3, DomainPlatformType.MOBILE.getName());
                ps.setByte(4, DomainPlatformType.MOBILE.getIsDesktop());
                ps.setByte(5, DomainPlatformType.MOBILE.getIsWeb());
                ps.setByte(6, DomainPlatformType.MOBILE.getIsMobile());
                ps.executeUpdate();
            }


            //DOMAIN_SIDES
            fieldsNames = new String[]{"id", "is_sys_rec", "name", "is_server", "is_client"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(DomainSide.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, DomainSide.SERVER_SIDE_ID);
                ps.setByte(2, DomainSide.SERVER_SIDE.getIsSysRec());
                ps.setString(3, DomainSide.SERVER_SIDE.getName());
                ps.setByte(4, DomainSide.SERVER_SIDE.getIsServer());
                ps.setByte(5, DomainSide.SERVER_SIDE.getIsClient());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, DomainSide.CLIENT_SIDE_ID);
                ps.setByte(2, DomainSide.CLIENT_SIDE.getIsSysRec());
                ps.setString(3, DomainSide.CLIENT_SIDE.getName());
                ps.setByte(4, DomainSide.CLIENT_SIDE.getIsServer());
                ps.setByte(5, DomainSide.CLIENT_SIDE.getIsClient());
                ps.executeUpdate();
            }

            //DOMAIN_TYPES
            fieldsNames = new String[]{"id", "is_sys_rec", "name", "description"};
            valuesBinders = new String[]{"?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(DomainType.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            for (DomainType domainType : new DomainType[]{
                    DomainType.ECOSYSTEM,
                    DomainType.APPLICATION,
                    DomainType.SERVICE,
                    DomainType.DATABASE,
                    DomainType.CLUSTER
            }) {
                try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                    ps.setLong(1, domainType.getId());
                    ps.setByte(2, domainType.getIsSysRec());
                    ps.setString(3, domainType.getName());
                    ps.setString(4, domainType.getDescription());
                    ps.executeUpdate();
                }
            }

            //DOMAINS
            fieldsNames = new String[]{"id", "is_sys_rec", "domain_type_id", "domain_platform_id", "domain_side_id", "name"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(Domain.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            for (Domain domain : new Domain[]{Domain.SSO_SERVER, Domain.SSO_WEBCLIENT}) {
                try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                    ps.setLong(1, domain.getId());
                    ps.setByte(2, domain.getIsSysRec());
                    ps.setLong(3, domain.getDomainTypeId());
                    ps.setObject(4, domain.getDomainPlatformId());
                    ps.setObject(5, domain.getDomainSideId());
                    ps.setString(6, domain.getName());
                    ps.executeUpdate();
                }
            }

            //RESOURCE_TYPES
            fieldsNames = new String[]{"id", "is_sys_rec", "name"};
            valuesBinders = new String[]{"?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(ResourceType.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, ResourceType.DOMAIN_ID);
                ps.setByte(2, ResourceType.DOMAIN.getIsSysRec());
                ps.setString(3, ResourceType.DOMAIN.getName());
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
            fieldsNames = new String[]{"id", "is_sys_rec", "parent_id", "domain_id", "resource_type_id", "name", "resource_path", "icon", "numeric_order"};
            valuesBinders = new String[]{"?", "?", "?","?", "?", "?","?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(Resource.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);

            //SSO WEBCLIENT RESOURCES
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.DOMAINS_ID);
                ps.setByte(2, Resource.DOMAINS.getIsSysRec());
                ps.setObject(3, Resource.DOMAINS.getParentId());
                ps.setLong(4, Resource.DOMAINS.getDomainId());
                ps.setLong(5, Resource.DOMAINS.getResourceTypeId());
                ps.setString(6, Resource.DOMAINS.getName());
                ps.setString(7, Resource.DOMAINS.getResourcePath());
                ps.setString(8, Resource.DOMAINS.getIcon());
                ps.setInt(9, Resource.DOMAINS.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.DOMAIN_TYPES_ID);
                ps.setByte(2, Resource.DOMAIN_TYPES.getIsSysRec());
                ps.setObject(3, Resource.DOMAIN_TYPES.getParentId());
                ps.setLong(4, Resource.DOMAIN_TYPES.getDomainId());
                ps.setLong(5, Resource.DOMAIN_TYPES.getResourceTypeId());
                ps.setString(6, Resource.DOMAIN_TYPES.getName());
                ps.setString(7, Resource.DOMAIN_TYPES.getResourcePath());
                ps.setString(8, Resource.DOMAIN_TYPES.getIcon());
                ps.setInt(9, Resource.DOMAIN_TYPES.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.ACCESS_PROFILES_ID);
                ps.setByte(2, Resource.ACCESS_PROFILES.getIsSysRec());
                ps.setObject(3, Resource.ACCESS_PROFILES.getParentId());
                ps.setLong(4, Resource.ACCESS_PROFILES.getDomainId());
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
                ps.setLong(4, Resource.AGENTS.getDomainId());
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
                ps.setLong(4, Resource.RESOURCES.getDomainId());
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
                ps.setLong(4, Resource.RESOURCE_PERMISSIONS.getDomainId());
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
                ps.setLong(4, Resource.RELATIONSHIPS.getDomainId());
                ps.setLong(5, Resource.RELATIONSHIPS.getResourceTypeId());
                ps.setString(6, Resource.RELATIONSHIPS.getName());
                ps.setString(7, Resource.RELATIONSHIPS.getResourcePath());
                ps.setString(8, Resource.RELATIONSHIPS.getIcon());
                ps.setInt(9, Resource.RELATIONSHIPS.getNumericOrder());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS_ID);
                ps.setByte(2, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getIsSysRec());
                ps.setObject(3, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getParentId());
                ps.setLong(4, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getDomainId());
                ps.setLong(5, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getResourceTypeId());
                ps.setString(6, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getName());
                ps.setString(7, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getResourcePath());
                ps.setString(8, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getIcon());
                ps.setInt(9, Resource.AGENTS_X_ACCESS_PROFILES_X_DOMAINS.getNumericOrder());
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
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setLong(1, AccessProfile.DEFAULT_ID);
                ps.setByte(2, AccessProfile.DEFAULT.getIsSysRec());
                ps.setString(3, AccessProfile.DEFAULT.getName());
                ps.executeUpdate();
            }


            //AGENTS_X_ACCESS_PROFILES_X_DOMAINS
            fieldsNames = new String[]{"is_sys_rec", "agent_id","access_profile_id", "domain_id"};
            valuesBinders = new String[]{"?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(AgentXAccessProfileXDomain.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte)1);
                ps.setLong(2, Agent.SYSTEM_ID);
                ps.setLong(3, AccessProfile.SYSTEM_ID);
                ps.setLong(4, Domain.SSO_WEBCLIENT_ID);
                ps.executeUpdate();
            }

            //RESOURCE_PERMISSIONS
            fieldsNames = new String[]{"is_sys_rec", "resource_id", "access_profile_id, allowed_access, allowed_view, allowed_create, allowed_change, allowed_delete"};
            valuesBinders = new String[]{"?", "?", "?", "?", "?", "?", "?", "?"};
            query = "insert ignore into " + JpaReflectionUtils.resolveTableName(ResourcePermission.class) + "(" + StringUtils.join(fieldsNames) + ") values (" + StringUtils.join(valuesBinders) + ")";
            logger.debug("Executing query: {}", query);
            /*try (PreparedStatement ps = context.getConnection().prepareStatement(query)) {
                ps.setByte(1, (byte) 1);
                ps.setLong(2, Resource.DOMAINS_ID);
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

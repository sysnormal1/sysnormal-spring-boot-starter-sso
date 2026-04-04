package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import jakarta.persistence.criteria.JoinType;

import java.util.List;

public interface ResourcesRepositoryCustom {

    List<ResourcePermissionView> findResourcePermissions(
            Long systemId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths,
            JoinType joinType
    );

    List<ResourcePermissionView> findResourcePermissions(
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths,
            JoinType joinType
    );

    List<ResourcePermissionView> findAlloweds(
            Long systemId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete
    );
}

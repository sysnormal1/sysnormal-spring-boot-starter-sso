package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import jakarta.persistence.criteria.JoinType;

import java.util.List;

public interface ResourcesRepositoryCustom {

    /*
     * includeDeleted: also brings the rows with deleted_at filled, of the resource or of the permission
     * includeExpired: also brings the rows outside the start_at / end_at window
     * includeInactive: also brings the rows whose record_status is not flagged active
     *
     * All three default to false at the caller: a grant that was revoked, that expired or that was
     * deactivated must not reach who asked for what is allowed.
     */

    List<ResourcePermissionView> findResourcePermissions(
            Long domainId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths,
            JoinType joinType,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    );

    List<ResourcePermissionView> findResourcePermissions(
            List<Long> domainIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths,
            JoinType joinType,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    );

    List<ResourcePermissionView> findAlloweds(
            Long domainId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    );
}

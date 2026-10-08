package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import java.util.List;

public interface ResourcesRepositoryCustom {

    /*
     * includeDeleted: also brings the rows with deleted_at filled, of the resource or of the permission
     * includeExpired: also brings the rows outside the start_at / end_at window
     * includeInactive: also brings the rows whose record_status is not flagged active
     *
     * All three default to false at the caller: a grant that was revoked, that expired or that was
     * deactivated must not reach who asked for what is allowed.
     *
     * effective: true answers "what reaches this agent" - a resource comes back when an applicable grant
     * reaches the agent, or when none does and the resource is not restricted (is_restricted = 0), with the
     * open-by-default flags. false answers "what is registered" - every resource of the filter, with the
     * grants that match it or with a null grant, for the administration screen.
     */

    List<ResourcePermissionView> findResourcePermissions(
            Long domainId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths,
            boolean effective,
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
            boolean effective,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    );

    /*
     * Always effective. The allowed* filters are evaluated over the effective flags - the ones of the
     * applicable grant, or the open-by-default ones when there is none -, never over whatever grant row
     * happens to join.
     */
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

    /*
     * A TABLE resource is identified by its name by the consumers, so two of them with the same name in the
     * same domain would protect the same table with different grants. Deleted ones do not count.
     * ignoreId: the resource being saved, null when it is new.
     */
    boolean existsTableResourceWithName(Long domainId, String name, Long ignoreId);
}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import java.util.List;

public interface ResourcesRepositoryCustom {

    List<ResourcePermissionView> findResourcePermissions(
            Long systemId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths
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

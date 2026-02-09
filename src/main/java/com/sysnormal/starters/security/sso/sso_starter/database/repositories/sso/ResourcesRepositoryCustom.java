package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import java.util.List;

public interface ResourcesRepositoryCustom {

    List<ResourcePermissionView> findWithPermissions(
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths
    );

    List<ResourcePermissionView> findAlloweds(
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete
    );
}

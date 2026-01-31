package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

public record ResourcePermissionView(
        Long resourceId,
        Long resourceParentId,
        String resourceName,
        Long permissionId,
        Long permissionAccessProfileId,
        Long permissionAgentId,
        Byte permissionAllowedView,
        Byte permissionAllowedAccess,
        Byte permissionAllowedCreate,
        Byte permissionAllowedChange,
        Byte permissionAllowedDelete
) {}
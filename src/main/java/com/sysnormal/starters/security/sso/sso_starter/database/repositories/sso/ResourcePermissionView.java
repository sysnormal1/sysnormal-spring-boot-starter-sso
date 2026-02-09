package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

public record ResourcePermissionView(
        Long resourceSystemId,
        Long resourceId,
        Long resourceParentId,
        String resourceName,
        String resourcePath,
        String resourceIcon,
        Long resourcePermissionId,
        Long resourcePermissionAccessProfileId,
        Long resourcePermissionAgentId,
        Byte resourcePermissionAllowedAccess,
        Byte resourcePermissionAllowedView,
        Byte resourcePermissionAllowedCreate,
        Byte resourcePermissionAllowedChange,
        Byte resourcePermissionAllowedDelete
) {}
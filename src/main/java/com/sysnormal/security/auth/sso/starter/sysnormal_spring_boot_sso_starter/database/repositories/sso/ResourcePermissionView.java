package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso;

public record ResourcePermissionView(
        Long resourceSystemId,
        Long resourceId,
        Long resourceParentId,
        Long resourceTypeId,
        String resourceName,
        String resourcePath,
        String resourceIcon,
        Integer resourceNumericOrder,
        Byte resourceShowInMenu,
        Long resourcePermissionId,
        Long resourcePermissionAccessProfileId,
        Long resourcePermissionAgentId,
        Byte resourcePermissionAllowedAccess,
        Byte resourcePermissionAllowedView,
        Byte resourcePermissionAllowedCreate,
        Byte resourcePermissionAllowedChange,
        Byte resourcePermissionAllowedDelete
) {}
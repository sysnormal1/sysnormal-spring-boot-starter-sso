package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import java.time.LocalDateTime;

public record ResourcePermissionView(
        Long resourceDomainId,
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
        Byte resourcePermissionAllowedDelete,

        //the filter is conditional, the column is constant: these four come always, so that a caller
        //that asked for the deleted or expired ones can tell them apart from the live ones
        LocalDateTime resourceDeletedAt,
        LocalDateTime resourcePermissionDeletedAt,
        LocalDateTime resourcePermissionStartAt,
        LocalDateTime resourcePermissionEndAt
) {}

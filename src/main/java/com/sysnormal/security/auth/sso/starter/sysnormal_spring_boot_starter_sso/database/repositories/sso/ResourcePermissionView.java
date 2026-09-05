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
        LocalDateTime resourcePermissionEndAt,

        /*
         * Columns that came after this projection was written, and that the consumer needs to act on the grant.
         *
         * resourceIdAtOrigin: what the resource points at in the domain that owns it - the table in the catalog
         * of a TABLE resource, the team of a DATA resource. Without it the name is all the consumer has.
         *
         * resourcePermissionJsonData: the condition, the normal path. resourcePermissionCondition: the same
         * condition in opaque text, the escape. A consumer that reads the grant without them applies no
         * restriction at all, and does not know it is missing one.
         *
         * Appended at the end on purpose: cb.construct is positional, so a field inserted in the middle would
         * silently shift every one after it.
         */
        String resourceIdAtOrigin,
        String resourceDescription,
        String resourceJsonData,
        String resourcePermissionCondition,
        String resourcePermissionJsonData
) {}

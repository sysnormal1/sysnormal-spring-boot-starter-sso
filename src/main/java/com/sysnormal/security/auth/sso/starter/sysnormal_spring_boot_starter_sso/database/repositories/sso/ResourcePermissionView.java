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
        String resourcePermissionJsonData,

        /*
         * Same reason as the four state columns above: record_status became a filter with an escape, so it
         * is state and not audit trivia. Asking for the inactive ones and not being able to tell which is
         * which would be no better than not being able to ask.
         */
        Long resourceRecordStatusId,
        Long resourcePermissionRecordStatusId,

        /*
         * What holds for the agent that no grant reaches: 1 denies, 0 allows. It is the only way a consumer
         * can tell a row with resourcePermissionId null - an open resource nobody granted - from a grant.
         */
        Byte resourceIsRestricted
) {

    /*
     * The flags of an open resource that no applicable grant reaches.
     *
     * Access and view, and never create, change or delete: on a data table the difference does not show, on a
     * registration screen it does - opening all five would hand deletion to everyone who can open the screen.
     * Whoever has to write on an open resource gets an explicit grant, as before.
     */
    public static final byte OPEN_BY_DEFAULT_ALLOWED_ACCESS = 1;
    public static final byte OPEN_BY_DEFAULT_ALLOWED_VIEW = 1;
    public static final byte OPEN_BY_DEFAULT_ALLOWED_CREATE = 0;
    public static final byte OPEN_BY_DEFAULT_ALLOWED_CHANGE = 0;
    public static final byte OPEN_BY_DEFAULT_ALLOWED_DELETE = 0;

    public ResourcePermissionView withOpenByDefaultFlags() {
        return new ResourcePermissionView(
                resourceDomainId, resourceId, resourceParentId, resourceTypeId, resourceName, resourcePath,
                resourceIcon, resourceNumericOrder, resourceShowInMenu,
                resourcePermissionId, resourcePermissionAccessProfileId, resourcePermissionAgentId,
                OPEN_BY_DEFAULT_ALLOWED_ACCESS,
                OPEN_BY_DEFAULT_ALLOWED_VIEW,
                OPEN_BY_DEFAULT_ALLOWED_CREATE,
                OPEN_BY_DEFAULT_ALLOWED_CHANGE,
                OPEN_BY_DEFAULT_ALLOWED_DELETE,
                resourceDeletedAt, resourcePermissionDeletedAt, resourcePermissionStartAt, resourcePermissionEndAt,
                resourceIdAtOrigin, resourceDescription, resourceJsonData,
                resourcePermissionCondition, resourcePermissionJsonData,
                resourceRecordStatusId, resourcePermissionRecordStatusId,
                resourceIsRestricted
        );
    }
}

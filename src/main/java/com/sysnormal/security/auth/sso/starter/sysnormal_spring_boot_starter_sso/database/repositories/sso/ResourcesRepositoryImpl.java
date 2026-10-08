package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AgentXAccessProfileXDomain;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Resource;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.RecordStatus;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourcePermission;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ResourcesRepositoryImpl implements ResourcesRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public void getResourcePermissionJoin(
            CriteriaBuilder builder,
            CriteriaQuery<ResourcePermissionView> query,
            Root<Resource> resource,
            Join<Resource, ResourcePermission> joinPermission,
            List<Predicate> joinPredicates,
            List<Long> agentIds,
            List<Long> accessProfileIds,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    ) {
        if (agentIds != null &&  !agentIds.isEmpty()) {
            if (accessProfileIds != null &&  !accessProfileIds.isEmpty()) {
                joinPredicates.add(joinPermission.get("agentId").in(agentIds));
                joinPredicates.add(joinPermission.get("accessProfileId").in(accessProfileIds));
            } else {
                Subquery<Integer> sqAgentPermission = query.subquery(Integer.class);
                Root<ResourcePermission> r2 = sqAgentPermission.from(ResourcePermission.class);

                List<Predicate> sqAgentPredicates = new ArrayList<>();
                sqAgentPredicates.add(builder.equal(r2.get("resourceId"), resource.get("id")));
                sqAgentPredicates.add(r2.get("agentId").in(agentIds));
                //a nominal grant that was revoked, expired or deactivated is not applicable, so it cannot hide the
                //grant of the profile either. Without this the resource vanished - and on an open resource,
                //vanishing the grant of the profile means falling back to open, dropping its predicate
                getPermissionVisibilityPredicates(builder, sqAgentPermission, r2, sqAgentPredicates,
                        includeDeleted, includeExpired, includeInactive);

                sqAgentPermission.select(builder.literal(1));
                sqAgentPermission.where(sqAgentPredicates.toArray(Predicate[]::new));

                Subquery<Long> sqAccessProfiles = query.subquery(Long.class);
                Root<AgentXAccessProfileXDomain> aas = sqAccessProfiles.from(AgentXAccessProfileXDomain.class);

                sqAccessProfiles.select(aas.get("accessProfileId"));
                sqAccessProfiles.where(
                        builder.equal(aas.get("domainId"), resource.get("domainId")),
                        aas.get("agentId").in(agentIds)
                );

                Predicate agentMatch = joinPermission.get("agentId").in(agentIds);

                Predicate profileFallback = builder.and(
                        //a grant nominal to someone else is not mine, even when it carries a profile i also have:
                        //without this the agent_id of the row is never looked at on this branch, and the grant leaks
                        joinPermission.get("agentId").isNull(),
                        builder.not(builder.exists(sqAgentPermission)),
                        joinPermission.get("accessProfileId").in(sqAccessProfiles)
                );

                joinPredicates.add(builder.and(builder.or(agentMatch, profileFallback)));
            }
        } else {
            joinPredicates.add(joinPermission.get("agentId").isNull()); //protect to get specific permissions of agents without parametrized in request
            if (accessProfileIds != null &&  !accessProfileIds.isEmpty()) {
                joinPredicates.add(joinPermission.get("accessProfileId").in(accessProfileIds));
            } else {
                joinPredicates.add(joinPermission.get("accessProfileId").isNull()); //protect do get all permissions if not parametrized in request
            }
        }
    }

    /*
     * Hides what was revoked, what is out of the validity window and what is not in an active status,
     * unless the caller asked otherwise.
     *
     * The predicate of the RESOURCE goes to the where, the one of the PERMISSION goes to the join.
     * The join is LEFT - to list a resource with no grant at all, and to let an open resource come back when
     * no grant reaches the agent - and a where over a column of the right side would degrade that LEFT into
     * an INNER, dropping exactly those resources.
     */
    public void getVisibilityPredicates(
            CriteriaBuilder builder,
            CriteriaQuery<ResourcePermissionView> query,
            Root<Resource> resource,
            Join<Resource, ResourcePermission> joinPermission,
            List<Predicate> rootPredicates,
            List<Predicate> joinPredicates,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    ) {
        if (!includeDeleted) {
            rootPredicates.add(builder.isNull(resource.get("deletedAt")));
        }
        if (!includeInactive) {
            rootPredicates.add(resource.get("recordStatusId").in(activeRecordStatusIds(builder, query)));
        }
        getPermissionVisibilityPredicates(builder, query, joinPermission, joinPredicates,
                includeDeleted, includeExpired, includeInactive);
    }

    /*
     * The part of the visibility that is about a grant, wherever the grant is read from: the joined one, or the
     * one a subquery looks for.
     */
    private void getPermissionVisibilityPredicates(
            CriteriaBuilder builder,
            CommonAbstractCriteria query,
            Path<ResourcePermission> permission,
            List<Predicate> predicates,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    ) {
        if (!includeDeleted) {
            predicates.add(builder.isNull(permission.get("deletedAt")));
        }
        if (!includeExpired) {
            LocalDateTime now = LocalDateTime.now();
            //null on either end means no limit on that side
            predicates.add(builder.or(
                    builder.isNull(permission.get("startAt")),
                    builder.lessThanOrEqualTo(permission.<LocalDateTime>get("startAt"), now)
            ));
            predicates.add(builder.or(
                    builder.isNull(permission.get("endAt")),
                    builder.greaterThanOrEqualTo(permission.<LocalDateTime>get("endAt"), now)
            ));
        }
        if (!includeInactive) {
            predicates.add(permission.get("recordStatusId").in(activeRecordStatusIds(builder, query)));
        }
    }

    /*
     * The ids of record_status that count as active.
     *
     * By is_active, and not by record_status_id = ACTIVE_ID: the status table is open to new rows, and a
     * status added later and flagged active would be hidden by a hardcoded id - silently, since a resource
     * that stops being returned looks the same as a resource nobody granted.
     *
     * A subquery, and not the recordStatus association of BaseSsoEntity: navigating the association would
     * make Hibernate add an implicit join, and an implicit join hanging off the permission side would
     * degrade the LEFT join into an INNER one. record_status has two rows, so the subquery costs nothing.
     */
    private Subquery<Long> activeRecordStatusIds(
            CriteriaBuilder builder,
            CommonAbstractCriteria query
    ) {
        Subquery<Long> sqActiveStatus = query.subquery(Long.class);
        Root<RecordStatus> rs = sqActiveStatus.from(RecordStatus.class);

        sqActiveStatus.select(rs.<Long>get("id"));
        sqActiveStatus.where(builder.equal(rs.get("isActive"), (byte) 1));

        return sqActiveStatus;
    }

    /*
     * Whether the open-by-default flags satisfy the allowed* filters the caller sent. Decided here and not in
     * sql because the default is a constant: a caller asking for allowedDelete = 1 can never be answered by
     * a resource that is only open by default.
     */
    private static boolean openByDefaultMatches(
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete
    ) {
        return (allowedAccess == null || allowedAccess == ResourcePermissionView.OPEN_BY_DEFAULT_ALLOWED_ACCESS)
                && (allowedView == null || allowedView == ResourcePermissionView.OPEN_BY_DEFAULT_ALLOWED_VIEW)
                && (allowedCreate == null || allowedCreate == ResourcePermissionView.OPEN_BY_DEFAULT_ALLOWED_CREATE)
                && (allowedChange == null || allowedChange == ResourcePermissionView.OPEN_BY_DEFAULT_ALLOWED_CHANGE)
                && (allowedDelete == null || allowedDelete == ResourcePermissionView.OPEN_BY_DEFAULT_ALLOWED_DELETE);
    }

    private List<ResourcePermissionView> find(
            List<Long> domainIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths,
            boolean effective,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourcePermissionView> cq = cb.createQuery(ResourcePermissionView.class);

        Root<Resource> r = cq.from(Resource.class);

        //LEFT in both modes. In the effective one the join only decides WHICH grant applies; whether the resource
        //comes back is decided in the where, below
        Join<Resource, ResourcePermission> p = r.join("resourcePermissions", JoinType.LEFT);

        List<Predicate> rootPredicates = new ArrayList<>();
        List<Predicate> joinPredicates = new ArrayList<>();
        if (domainIds != null && !domainIds.isEmpty()) {
            rootPredicates.add(r.get("domainId").in(domainIds));
        }
        if (resourceTypeIds != null && !resourceTypeIds.isEmpty()) {
            rootPredicates.add(r.get("resourceTypeId").in(resourceTypeIds));
        }
        if (resourcePaths != null && !resourcePaths.isEmpty()) {
            rootPredicates.add(r.get("resourcePath").in(resourcePaths));
        }

        getResourcePermissionJoin(cb, cq, r, p, joinPredicates, agentIds, accessProfileIds,
                includeDeleted, includeExpired, includeInactive);

        getVisibilityPredicates(cb, cq, r, p, rootPredicates, joinPredicates,
                includeDeleted, includeExpired, includeInactive);

        if (!joinPredicates.isEmpty()) {
            p.on(cb.and(joinPredicates.toArray(Predicate[]::new)));
        }

        if (effective) {
            /*
             * The flags filter is over the flags of the grant that applies, so it lives here and NEVER in the on.
             * In the on, a grant that denies (allowed_access = 0) would simply fail to join, the right side would
             * come null, and the row would look exactly like "no grant applies" - which on an open resource
             * means allowed. Every explicit denial would turn into access.
             */
            List<Predicate> grantPredicates = new ArrayList<>();
            grantPredicates.add(p.get("id").isNotNull());
            if (allowedAccess != null) {
                grantPredicates.add(cb.equal(p.get("allowedAccess"), allowedAccess));
            }
            if (allowedView != null) {
                grantPredicates.add(cb.equal(p.get("allowedView"), allowedView));
            }
            if (allowedCreate != null) {
                grantPredicates.add(cb.equal(p.get("allowedCreate"), allowedCreate));
            }
            if (allowedChange != null) {
                grantPredicates.add(cb.equal(p.get("allowedChange"), allowedChange));
            }
            if (allowedDelete != null) {
                grantPredicates.add(cb.equal(p.get("allowedDelete"), allowedDelete));
            }
            Predicate grantApplies = cb.and(grantPredicates.toArray(Predicate[]::new));

            if (openByDefaultMatches(allowedAccess, allowedView, allowedCreate, allowedChange, allowedDelete)) {
                Predicate openByDefault = cb.and(
                        p.get("id").isNull(),
                        cb.equal(r.get("isRestricted"), (byte) 0)
                );
                rootPredicates.add(cb.or(grantApplies, openByDefault));
            } else {
                rootPredicates.add(grantApplies);
            }
        }

        cq.select(cb.construct(
                ResourcePermissionView.class, //must maintain same order of fields in class
                r.get("domainId").alias("resourceDomainId"),
                r.get("id").alias("resourceId"),
                r.get("parentId").alias("resourceParentId"),
                r.get("resourceTypeId").alias("resourceTypeId"),
                r.get("name").alias("resourceName"),
                r.get("resourcePath").alias("resourcePath"),
                r.get("icon").alias("resourceIcon"),
                r.get("numericOrder").alias("resourceNumericOrder"),
                r.get("showInMenu").alias("resourceShowInMenu"),
                p.get("id").alias("resourcePermissionId"),
                p.get("accessProfileId").alias("resourcePermissionAccessProfileId"),
                p.get("agentId").alias("resourcePermissionAgentId"),
                p.get("allowedAccess").alias("resourcePermissionAllowedAccess"),
                p.get("allowedView").alias("resourcePermissionAllowedView"),
                p.get("allowedCreate").alias("resourcePermissionAllowedCreate"),
                p.get("allowedChange").alias("resourcePermissionAllowedChange"),
                p.get("allowedDelete").alias("resourcePermissionAllowedDelete"),
                r.get("deletedAt").alias("resourceDeletedAt"),
                p.get("deletedAt").alias("resourcePermissionDeletedAt"),
                p.get("startAt").alias("resourcePermissionStartAt"),
                p.get("endAt").alias("resourcePermissionEndAt"),
                r.get("idAtOrigin").alias("resourceIdAtOrigin"),
                r.get("description").alias("resourceDescription"),
                r.get("jsonData").alias("resourceJsonData"),
                p.get("condition").alias("resourcePermissionCondition"),
                p.get("jsonData").alias("resourcePermissionJsonData"),
                r.get("recordStatusId").alias("resourceRecordStatusId"),
                p.get("recordStatusId").alias("resourcePermissionRecordStatusId"),
                r.get("isRestricted").alias("resourceIsRestricted")
        ));

        cq.where(rootPredicates.toArray(Predicate[]::new));

        cq.orderBy(
                cb.asc(r.get("domainId")),
                cb.asc(p.get("accessProfileId")),
                cb.asc(p.get("agentId")),
                cb.asc(cb.coalesce(
                        r.get("numericOrder"),
                        r.get("id")
                ))
        );

        List<ResourcePermissionView> result = em.createQuery(cq).getResultList();
        if (effective) {
            //in the effective mode a null grant can only be an open resource that nobody granted (the where above
            //guarantees it), and the consumer needs the flags that hold for it, not nulls
            result = result.stream()
                    .map(v -> v.resourcePermissionId() == null ? v.withOpenByDefaultFlags() : v)
                    .toList();
        }
        return result;
    }

    @Override
    public List<ResourcePermissionView> findResourcePermissions(
            List<Long> domainIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths,
            boolean effective,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    ) {
        return find(domainIds, resourceTypeIds, accessProfileIds, agentIds, resourcePaths, effective,
                null, null, null, null, null,
                includeDeleted, includeExpired, includeInactive);
    }

    @Override
    public List<ResourcePermissionView> findResourcePermissions(
            Long domainId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths,
            boolean effective,
            boolean includeDeleted,
            boolean includeExpired,
            boolean includeInactive
    ) {
        return findResourcePermissions(listOf(domainId), listOf(resourceTypeId), listOf(accessProfileId),
                listOf(agentId), resourcePaths, effective, includeDeleted, includeExpired, includeInactive);
    }

    @Override
    public List<ResourcePermissionView> findAlloweds(
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
    ) {
        return find(listOf(domainId), listOf(resourceTypeId), listOf(accessProfileId), listOf(agentId), null, true,
                allowedAccess, allowedView, allowedCreate, allowedChange, allowedDelete,
                includeDeleted, includeExpired, includeInactive);
    }

    /*
     * readOnly on purpose: it runs in the middle of a patch, over an entity already changed in memory, and a
     * read-only transaction keeps Hibernate from flushing that change before the check has decided anything.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsTableResourceWithName(Long domainId, String name, Long ignoreId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Resource> r = cq.from(Resource.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(r.get("domainId"), domainId));
        predicates.add(cb.equal(r.get("resourceTypeId"), ResourceType.TABLE_RESOURCE_ID));
        predicates.add(cb.equal(r.get("name"), name));
        predicates.add(cb.isNull(r.get("deletedAt")));
        if (ignoreId != null) {
            predicates.add(cb.notEqual(r.get("id"), ignoreId));
        }

        cq.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
        return em.createQuery(cq).getSingleResult() > 0;
    }

    private static <T> List<T> listOf(T value) {
        return value == null ? null : List.of(value);
    }

}

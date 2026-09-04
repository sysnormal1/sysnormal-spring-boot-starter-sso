package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AgentXAccessProfileXDomain;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Resource;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourcePermission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

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
            List<Long> accessProfileIds
    ) {
        if (agentIds != null &&  !agentIds.isEmpty()) {
            if (accessProfileIds != null &&  !accessProfileIds.isEmpty()) {
                joinPredicates.add(joinPermission.get("agentId").in(agentIds));
                joinPredicates.add(joinPermission.get("accessProfileId").in(accessProfileIds));
            } else {
                Subquery<Integer> sqAgentPermission = query.subquery(Integer.class);
                Root<ResourcePermission> r2 = sqAgentPermission.from(ResourcePermission.class);

                sqAgentPermission.select(builder.literal(1));
                sqAgentPermission.where(
                        builder.equal(r2.get("resourceId"), resource.get("id")),
                        r2.get("agentId").in(agentIds)
                );

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
     * Hides what was revoked and what is out of the validity window, unless the caller asked otherwise.
     *
     * The predicate of the RESOURCE goes to the where, the one of the PERMISSION goes to the join.
     * In getWithPermissions the join is LEFT on purpose - to list a resource with no grant at all - and a
     * where over a column of the right side would degrade that LEFT into an INNER, dropping exactly the
     * resources the administration screen needs to show.
     */
    public void getVisibilityPredicates(
            CriteriaBuilder builder,
            Root<Resource> resource,
            Join<Resource, ResourcePermission> joinPermission,
            List<Predicate> rootPredicates,
            List<Predicate> joinPredicates,
            boolean includeDeleted,
            boolean includeExpired
    ) {
        if (!includeDeleted) {
            rootPredicates.add(builder.isNull(resource.get("deletedAt")));
            joinPredicates.add(builder.isNull(joinPermission.get("deletedAt")));
        }
        if (!includeExpired) {
            LocalDateTime now = LocalDateTime.now();
            //null on either end means no limit on that side
            joinPredicates.add(builder.or(
                    builder.isNull(joinPermission.get("startAt")),
                    builder.lessThanOrEqualTo(joinPermission.<LocalDateTime>get("startAt"), now)
            ));
            joinPredicates.add(builder.or(
                    builder.isNull(joinPermission.get("endAt")),
                    builder.greaterThanOrEqualTo(joinPermission.<LocalDateTime>get("endAt"), now)
            ));
        }
    }

    @Override
    public List<ResourcePermissionView> findResourcePermissions(
            List<Long> domainIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths,
            JoinType joinType,
            boolean includeDeleted,
            boolean includeExpired
    ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourcePermissionView> cq = cb.createQuery(ResourcePermissionView.class);

        Root<Resource> r = cq.from(Resource.class);
        Join<Resource, ResourcePermission> p =
                r.join("resourcePermissions", joinType );

        List<Predicate> rootPredicates = new ArrayList<>();
        List<Predicate> joinPredicates = new ArrayList<>();
        if (domainIds != null && !domainIds.isEmpty()) {
            rootPredicates.add(r.get("domainId").in(domainIds));
        }
        if (resourceTypeIds != null && !resourceTypeIds.isEmpty()) {
            rootPredicates.add(r.get("resourceTypeId").in(resourceTypeIds));
        }

        getResourcePermissionJoin(
                cb,
                cq,
                r,
                p,
                joinPredicates,
                agentIds,
                accessProfileIds
        );

        getVisibilityPredicates(
                cb,
                r,
                p,
                rootPredicates,
                joinPredicates,
                includeDeleted,
                includeExpired
        );

        if (resourcePaths != null && !resourcePaths.isEmpty()) {
            rootPredicates.add(r.get("resourcePath").in(resourcePaths));
        }
        if (!joinPredicates.isEmpty()) {
            p.on(cb.and(joinPredicates.toArray(Predicate[]::new)));
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
                p.get("endAt").alias("resourcePermissionEndAt")

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

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<ResourcePermissionView> findResourcePermissions(
            Long domainId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths,
            JoinType joinType,
            boolean includeDeleted,
            boolean includeExpired
    ) {
        List<Long> domainIds = null;
        if (domainId != null) {
            domainIds = List.of(domainId);
        }
        List<Long> resourceTypeIds = null;
        if (resourceTypeId != null) {
            resourceTypeIds = List.of(resourceTypeId);
        }
        List<Long> accessProfileIds = null;
        if (accessProfileId != null) {
            accessProfileIds = List.of(accessProfileId);
        }
        List<Long> agentIds = null;
        if (agentId != null) {
            agentIds = List.of(agentId);
        }

        return findResourcePermissions(domainIds, resourceTypeIds, accessProfileIds, agentIds, resourcePaths, joinType, includeDeleted, includeExpired);
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
            boolean includeExpired
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourcePermissionView> cq = cb.createQuery(ResourcePermissionView.class);

        Root<Resource> r = cq.from(Resource.class);
        Join<Resource, ResourcePermission> p =
                r.join("resourcePermissions", JoinType.INNER );

        List<Predicate> rootPredicates = new ArrayList<>();
        List<Predicate> joinPredicates = new ArrayList<>();
        if (domainId != null) {
            rootPredicates.add(r.get("domainId").equalTo(domainId));
        }
        if (resourceTypeId != null) {
            rootPredicates.add(r.get("resourceTypeId").equalTo(resourceTypeId));
        }

        List<Long> agentIds = null;
        if (agentId != null) {
            agentIds = List.of(agentId);
        }
        List<Long> accessProfileIds = null;
        if (accessProfileId != null) {
            accessProfileIds = List.of(accessProfileId);
        }

        getResourcePermissionJoin(
                cb,
                cq,
                r,
                p,
                joinPredicates,
                agentIds,
                accessProfileIds
        );

        getVisibilityPredicates(
                cb,
                r,
                p,
                rootPredicates,
                joinPredicates,
                includeDeleted,
                includeExpired
        );


        if (allowedAccess != null) {
            joinPredicates.add(p.get("allowedAccess").equalTo(allowedAccess));
        }
        if (allowedView != null) {
            joinPredicates.add(p.get("allowedView").equalTo(allowedView));
        }
        if (allowedCreate != null) {
            joinPredicates.add(p.get("allowedCreate").equalTo(allowedCreate));
        }
        if (allowedChange != null) {
            joinPredicates.add(p.get("allowedChange").equalTo(allowedChange));
        }
        if (allowedDelete != null) {
            joinPredicates.add(p.get("allowedDelete").equalTo(allowedDelete));
        }

        if (!joinPredicates.isEmpty()) {
            p.on(joinPredicates.toArray(Predicate[]::new));
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
                p.get("endAt").alias("resourcePermissionEndAt")
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

        return em.createQuery(cq).getResultList();
    }


}

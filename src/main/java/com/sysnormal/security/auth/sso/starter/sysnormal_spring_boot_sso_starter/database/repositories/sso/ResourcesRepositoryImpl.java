package com.sysnormal.security.auth.sso.starter.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.AgentXAccessProfileXSystem;
import com.sysnormal.security.auth.sso.starter.database.entities.sso.Resource;
import com.sysnormal.security.auth.sso.starter.database.entities.sso.ResourcePermission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

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
            Long agentId,
            Long accessProfileId
    ) {
        if (agentId != null) {
            if (accessProfileId != null) {
                joinPredicates.add(joinPermission.get("agentId").equalTo(agentId));
                joinPredicates.add(joinPermission.get("accessProfileId").equalTo(accessProfileId));
            } else {
                Subquery<Integer> sqAgentPermission = query.subquery(Integer.class);
                Root<ResourcePermission> r2 = sqAgentPermission.from(ResourcePermission.class);

                sqAgentPermission.select(builder.literal(1));
                sqAgentPermission.where(
                        builder.equal(r2.get("resourceId"), resource.get("id")),
                        r2.get("agentId").equalTo(agentId)
                );

                Subquery<Long> sqAccessProfiles = query.subquery(Long.class);
                Root<AgentXAccessProfileXSystem> aas = sqAccessProfiles.from(AgentXAccessProfileXSystem.class);

                sqAccessProfiles.select(aas.get("accessProfileId"));
                sqAccessProfiles.where(
                        builder.equal(aas.get("systemId"), resource.get("systemId")),
                        aas.get("agentId").equalTo(agentId)
                );

                Predicate agentMatch = joinPermission.get("agentId").equalTo(agentId);

                Predicate profileFallback = builder.and(
                        builder.not(builder.exists(sqAgentPermission)),
                        joinPermission.get("accessProfileId").in(sqAccessProfiles)
                );

                joinPredicates.add(builder.and(builder.or(agentMatch, profileFallback)));
            }
        } else {
            joinPredicates.add(joinPermission.get("agentId").isNull()); //protect to get specific permissions of agents without parametrized in request
            if (accessProfileId != null) {
                joinPredicates.add(joinPermission.get("accessProfileId").equalTo(accessProfileId));
            } else {
                joinPredicates.add(joinPermission.get("accessProfileId").isNull()); //protect do get all permissions if not parametrized in request
            }
        }
    }

    @Override
    public List<ResourcePermissionView> findResourcePermissions(
            Long systemId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            List<String> resourcePaths
    ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourcePermissionView> cq = cb.createQuery(ResourcePermissionView.class);

        Root<Resource> r = cq.from(Resource.class);
        Join<Resource, ResourcePermission> p =
                r.join("resourcePermissions", JoinType.INNER );

        List<Predicate> rootPredicates = new ArrayList<>();
        List<Predicate> joinPredicates = new ArrayList<>();
        if (systemId != null) {
            rootPredicates.add(r.get("systemId").equalTo(systemId));
        }
        if (resourceTypeId != null) {
            rootPredicates.add(r.get("resourceTypeId").equalTo(resourceTypeId));
        }

        getResourcePermissionJoin(
                cb,
                cq,
                r,
                p,
                joinPredicates,
                agentId,
                accessProfileId
        );

        /*if (agentIds != null && !agentIds.isEmpty()) {
            if (accessProfileIds != null && !accessProfileIds.isEmpty()) {
                joinPredicates.add(p.get("agentId").in(agentIds));
                joinPredicates.add(p.get("accessProfileId").in(accessProfileIds));
            } else {
                Subquery<Integer> sqAgentPermission = cq.subquery(Integer.class);
                Root<ResourcePermission> r2 = sqAgentPermission.from(ResourcePermission.class);

                sqAgentPermission.select(cb.literal(1));
                sqAgentPermission.where(
                        cb.equal(r2.get("resourceId"), r.get("id")),
                        r2.get("agentId").in(agentIds)
                );

                Subquery<Long> sqAccessProfiles = cq.subquery(Long.class);
                Root<AgentXAccessProfileXSystem> aas = sqAccessProfiles.from(AgentXAccessProfileXSystem.class);

                sqAccessProfiles.select(aas.get("accessProfileId"));
                sqAccessProfiles.where(
                        cb.equal(aas.get("systemId"), r.get("systemId")),
                        aas.get("agentId").in(agentIds)
                );

                Predicate agentMatch = p.get("agentId").in(agentIds);

                Predicate profileFallback = cb.and(
                        cb.not(cb.exists(sqAgentPermission)),
                        p.get("accessProfileId").in(sqAccessProfiles)
                );

                joinPredicates.add(cb.and(cb.or(agentMatch, profileFallback)));
            }
        } else {
            joinPredicates.add(p.get("agentId").isNull()); //protect to get specific permissions of agents without parametrized in request
            if (accessProfileIds != null && !accessProfileIds.isEmpty()) {
                joinPredicates.add(p.get("accessProfileId").in(accessProfileIds));
            } else {
                joinPredicates.add(p.get("accessProfileId").isNull()); //protect do get all permissions if not parametrized in request
            }
        }*/

        if (resourcePaths != null && !resourcePaths.isEmpty()) {
            rootPredicates.add(r.get("resourcePath").in(resourcePaths));
        }
        if (!joinPredicates.isEmpty()) {
            p.on(cb.and(joinPredicates.toArray(Predicate[]::new)));
        }


        cq.select(cb.construct(
                ResourcePermissionView.class, //must maintain same order of fields in class
                r.get("systemId").alias("resourceSystemId"),
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
                p.get("allowedDelete").alias("resourcePermissionAllowedDelete")

        ));

        cq.where(rootPredicates.toArray(Predicate[]::new));

        cq.orderBy(
                cb.asc(r.get("systemId")),
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
    public List<ResourcePermissionView> findAlloweds(
            Long systemId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourcePermissionView> cq = cb.createQuery(ResourcePermissionView.class);

        Root<Resource> r = cq.from(Resource.class);
        Join<Resource, ResourcePermission> p =
                r.join("resourcePermissions", JoinType.INNER );

        List<Predicate> rootPredicates = new ArrayList<>();
        List<Predicate> joinPredicates = new ArrayList<>();
        if (systemId != null) {
            rootPredicates.add(r.get("systemId").equalTo(systemId));
        }
        if (resourceTypeId != null) {
            rootPredicates.add(r.get("resourceTypeId").equalTo(resourceTypeId));
        }

        /*if (accessProfileId != null) {
            joinPredicates.add(p.get("accessProfileId").equalTo(accessProfileIds));
        }
        if (agentIds != null && !agentIds.isEmpty()) {
            joinPredicates.add(p.get("agentId").in(agentIds));
        }*/

        getResourcePermissionJoin(
                cb,
                cq,
                r,
                p,
                joinPredicates,
                agentId,
                accessProfileId
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
                r.get("systemId").alias("resourceSystemId"),
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
                p.get("allowedDelete").alias("resourcePermissionAllowedDelete")
        ));

        cq.where(rootPredicates.toArray(Predicate[]::new));

        cq.orderBy(
                cb.asc(r.get("systemId")),
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

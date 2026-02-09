package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Resource;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.ResourcePermission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ResourcesRepositoryImpl
        implements ResourcesRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ResourcePermissionView> findWithPermissions(
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            List<String> resourcePaths
    ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourcePermissionView> cq = cb.createQuery(ResourcePermissionView.class);

        Root<Resource> r = cq.from(Resource.class);
        Join<Resource, ResourcePermission> p =
                r.join("resourcePermissions", JoinType.LEFT );

        List<Predicate> rootPredicates = new ArrayList<>();
        List<Predicate> joinPredicates = new ArrayList<>();
        if (systemIds != null && !systemIds.isEmpty()) {
            rootPredicates.add(r.get("systemId").in(systemIds));
        }
        if (resourceTypeIds != null && !resourceTypeIds.isEmpty()) {
            rootPredicates.add(r.get("resourceTypeId").in(resourceTypeIds));
        }
        List<Predicate> orPredicates = new ArrayList<>();
        if (accessProfileIds != null && !accessProfileIds.isEmpty()) {
            orPredicates.add(p.get("accessProfileId").in(accessProfileIds));
        }
        if (agentIds != null && !agentIds.isEmpty()) {
            orPredicates.add(p.get("agentId").in(agentIds));
        }
        if (!orPredicates.isEmpty()) {
            joinPredicates.add(
                    cb.or(orPredicates.toArray(Predicate[]::new))
            );
        }
        if (resourcePaths != null && !resourcePaths.isEmpty()) {
            rootPredicates.add(r.get("resourcePath").in(resourcePaths));
        }
        if (!joinPredicates.isEmpty()) {
            p.on(joinPredicates.toArray(Predicate[]::new));
        }


        cq.select(cb.construct(
                ResourcePermissionView.class, //must maintain same order of fields in class
                r.get("systemId").alias("resourceSystemId"),
                r.get("id").alias("resourceId"),
                r.get("parentId").alias("resourceParentId"),
                r.get("name").alias("resourceName"),
                r.get("resourcePath").alias("resourcePath"),
                r.get("icon").alias("resourceIcon"),
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
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
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
        if (systemIds != null && !systemIds.isEmpty()) {
            rootPredicates.add(r.get("systemId").in(systemIds));
        }
        if (resourceTypeIds != null && !resourceTypeIds.isEmpty()) {
            rootPredicates.add(r.get("resourceTypeId").in(resourceTypeIds));
        }
        if (accessProfileIds != null && !accessProfileIds.isEmpty()) {
            joinPredicates.add(p.get("accessProfileId").in(accessProfileIds));
        }
        if (agentIds != null && !agentIds.isEmpty()) {
            joinPredicates.add(p.get("agentId").in(agentIds));
        }
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
                r.get("name").alias("resourceName"),
                r.get("resourcePath").alias("resourcePath"),
                r.get("icon").alias("resourceIcon"),
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

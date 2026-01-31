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
            List<Long> agentIds) {

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
        if (accessProfileIds != null && !accessProfileIds.isEmpty()) {
            joinPredicates.add(p.get("accessProfileId").in(accessProfileIds));
        }
        if (agentIds != null && !agentIds.isEmpty()) {
            joinPredicates.add(p.get("agentId").in(agentIds));
        }
        if (!joinPredicates.isEmpty()) {
            p.on(joinPredicates.toArray(Predicate[]::new));
        }


        cq.select(cb.construct(
                ResourcePermissionView.class,
                r.get("id").alias("resourceId"),
                r.get("parentId").alias("resourceParentId"),
                r.get("name").alias("resourceName"),
                p.get("id").alias("permissionId"),
                p.get("accessProfileId").alias("permissionAccessProfileId"),
                p.get("agentId").alias("permissionAgentId"),
                p.get("allowedView").alias("permissionAllowedView"),
                p.get("allowedAccess").alias("permissionAllowedAccess"),
                p.get("allowedCreate").alias("permissionAllowedCreate"),
                p.get("allowedChange").alias("permissionAllowedChange"),
                p.get("allowedDelete").alias("permissionAllowedDelete")
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

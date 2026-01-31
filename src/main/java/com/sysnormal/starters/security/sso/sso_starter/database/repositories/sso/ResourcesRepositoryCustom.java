package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import jakarta.persistence.Tuple;

import java.util.List;
import java.util.Map;

public interface ResourcesRepositoryCustom {

    List<ResourcePermissionView> findWithPermissions(
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds
    );
}

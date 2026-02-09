package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.ResourcePermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * agents repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourcePermissionsRepository extends BaseSsoRepository<ResourcePermission, Long> {



}


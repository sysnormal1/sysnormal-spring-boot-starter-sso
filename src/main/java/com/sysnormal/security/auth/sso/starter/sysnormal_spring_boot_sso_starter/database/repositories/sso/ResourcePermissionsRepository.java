package com.sysnormal.security.auth.sso.starter.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.ResourcePermission;
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


package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourcePermission;
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


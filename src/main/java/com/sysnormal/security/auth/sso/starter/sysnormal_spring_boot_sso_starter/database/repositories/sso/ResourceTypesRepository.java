package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.ResourceType;
import org.springframework.stereotype.Repository;

/**
 * Resources repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourceTypesRepository extends BaseSsoRepository<ResourceType, Long> {


}


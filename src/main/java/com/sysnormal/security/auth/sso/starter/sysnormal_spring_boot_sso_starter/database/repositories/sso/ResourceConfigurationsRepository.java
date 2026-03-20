package com.sysnormal.security.auth.sso.starter.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.ResourceConfiguration;
import org.springframework.stereotype.Repository;

/**
 * Resources repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourceConfigurationsRepository extends BaseSsoRepository<ResourceConfiguration, Long> {


}


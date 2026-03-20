package com.sysnormal.security.auth.sso.starter.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.SystemPlatformType;
import org.springframework.stereotype.Repository;

/**
 * system platforms repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface SystemPlatformsRepository extends BaseSsoRepository<SystemPlatformType, Long> {


}


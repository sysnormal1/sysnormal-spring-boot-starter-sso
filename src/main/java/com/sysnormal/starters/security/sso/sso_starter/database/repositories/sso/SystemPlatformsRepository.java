package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.SystemPlatform;
import org.springframework.stereotype.Repository;

/**
 * system platforms repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface SystemPlatformsRepository extends BaseSsoRepository<SystemPlatform, Long> {


}


package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.AccessProfile;
import org.springframework.stereotype.Repository;

/**
 * systems repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface AccessProfilesRepository extends BaseSsoRepository<AccessProfile, Long> {


}


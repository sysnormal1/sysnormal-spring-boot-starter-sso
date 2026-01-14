package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Resource;
import org.springframework.stereotype.Repository;

/**
 * Resources repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourcesRepository extends BaseSsoRepository<Resource, Long> {


}


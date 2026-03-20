package com.sysnormal.security.auth.sso.starter.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.Resource;
import org.springframework.stereotype.Repository;

/**
 * Resources repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourcesRepository extends BaseSsoRepository<Resource, Long>, ResourcesRepositoryCustom  {


}


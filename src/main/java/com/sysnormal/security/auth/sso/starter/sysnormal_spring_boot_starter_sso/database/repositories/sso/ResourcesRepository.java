package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Resource;
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


package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.DomainSide;
import org.springframework.stereotype.Repository;

/**
 * domain sides repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface DomainSidesRepository extends BaseSsoRepository<DomainSide, Long> {


}


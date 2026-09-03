package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.DomainType;
import org.springframework.stereotype.Repository;

/**
 * domain types repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface DomainTypesRepository extends BaseSsoRepository<DomainType, Long> {


}

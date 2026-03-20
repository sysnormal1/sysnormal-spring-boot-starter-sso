package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.IdentifierType;
import org.springframework.stereotype.Repository;

/**
 * identifier types repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface IdentifierTypesRepository extends BaseSsoRepository<IdentifierType, Long> {


}


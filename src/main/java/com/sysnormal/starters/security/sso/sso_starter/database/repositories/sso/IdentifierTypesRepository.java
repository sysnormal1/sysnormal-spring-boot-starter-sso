package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.IdentifierType;
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


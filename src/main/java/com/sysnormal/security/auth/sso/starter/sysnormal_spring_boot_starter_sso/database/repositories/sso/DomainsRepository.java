package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Domain;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * domains repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface DomainsRepository extends BaseSsoRepository<Domain, Long> {

    Optional<Domain> findByName(String name);

}


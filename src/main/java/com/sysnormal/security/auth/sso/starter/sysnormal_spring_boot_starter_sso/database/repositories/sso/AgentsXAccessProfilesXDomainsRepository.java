package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AgentXAccessProfileXDomain;
import org.springframework.stereotype.Repository;

/**
 * AgentXAccessProfileXDomain repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface AgentsXAccessProfilesXDomainsRepository extends BaseSsoRepository<AgentXAccessProfileXDomain, Long> {

    boolean existsByAgentIdAndAccessProfileId(Long agentId, Long accessProfileId);

}


package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Agent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * agents repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface AgentsRepository extends BaseSsoRepository<Agent, Long> {

    Optional<Agent> findByIdentifierTypeIdAndIdentifier(Long identifierTypeId, String identifier);

    Optional<Agent> findByEmail(String email);

    @Query("""
        SELECT a FROM Agent a
        WHERE a.identifierTypeId = :identifierTypeId
          AND (a.identifier = :identifier OR a.email = :email)
    """)
    Optional<Agent> findByIdentifierTypeIdAndIdentifierOrEmail(
            @Param("identifierTypeId") Long identifierTypeId,
            @Param("identifier") String identifier,
            @Param("email") String email
    );

    @Query(value = """        
        SELECT 
            aa.access_profile_id
        FROM agents_x_access_profiles aa
        JOIN access_profilies_x_systems apy
            ON apy.access_profile_id = aa.access_profile_id
           AND apy.system_id = :systemId
        WHERE 
            aa.agent_id = :agentId
        """, nativeQuery = true)
    Long getRelationAccessProfile(
            @Param("agentId") Long agentId,
            @Param("systemId") Long systemId
    );

    @Query(value = """
        SELECT case when COUNT(1) > 0 then 1 else 0 end
        WHERE
            EXISTS (
                SELECT 1
                FROM agents_x_access_profiles aa
                JOIN access_profilies_x_systems apy
                    ON apy.access_profile_id = aa.access_profile_id
                   AND apy.system_id = :systemId
                WHERE aa.agent_id = :agentId
            )
        OR EXISTS (
            SELECT 1
            FROM agents_x_systems ay
            WHERE ay.agent_id = :agentId
              AND ay.system_id = :systemId
        )
        """, nativeQuery = true)
    byte agentIsRelationToSystem(
            @Param("agentId") Long agentId,
            @Param("systemId") Long systemId
    );

}


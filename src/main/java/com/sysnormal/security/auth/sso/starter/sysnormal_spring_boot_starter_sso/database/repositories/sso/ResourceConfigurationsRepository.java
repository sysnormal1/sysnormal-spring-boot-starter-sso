package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Agent;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourceConfiguration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Resources repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourceConfigurationsRepository extends BaseSsoRepository<ResourceConfiguration, Long> {


    @Query("""
        SELECT 
            rc 
        FROM 
            Resource r
            join ResourceConfiguration rc on rc.resourceId = r.id
        WHERE 
            r.name = :resourceName
    """)
    Optional<ResourceConfiguration> findByResourceName(
            @Param("resourceName") String resourceName
    );
}


package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agentsXAccessProfilesXSystems;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AgentXAccessProfileXSystem;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.AgentsXAccessProfilesXSystemsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AgentsXAccessProfilesXSystemsService extends BaseSsoRecordsService<AgentXAccessProfileXSystem, AgentsXAccessProfilesXSystemsRepository> {

    public AgentsXAccessProfilesXSystemsService(AgentsXAccessProfilesXSystemsRepository repository) {
        super(AgentXAccessProfileXSystem.class, repository);
    }

}

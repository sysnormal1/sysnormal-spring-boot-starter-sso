package com.sysnormal.security.auth.sso.starter.services.records.agentsXAccessProfilesXSystems;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.AgentXAccessProfileXSystem;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.AgentsXAccessProfilesXSystemsRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AgentsXAccessProfilesXSystemsService extends BaseSsoRecordsService<AgentXAccessProfileXSystem, AgentsXAccessProfilesXSystemsRepository> {

    public AgentsXAccessProfilesXSystemsService(AgentsXAccessProfilesXSystemsRepository repository) {
        super(AgentXAccessProfileXSystem.class, repository);
    }

}

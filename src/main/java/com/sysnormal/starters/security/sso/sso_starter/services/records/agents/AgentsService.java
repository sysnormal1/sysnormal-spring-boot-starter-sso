package com.sysnormal.starters.security.sso.sso_starter.services.records.agents;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Agent;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AgentsService extends BaseSsoRecordsService<Agent, AgentsRepository> {

    public AgentsService(AgentsRepository repository) {
        super(Agent.class, repository);
    }

}

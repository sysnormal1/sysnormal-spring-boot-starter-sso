package com.sysnormal.security.auth.sso.starter.services.records.agents;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.Agent;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AgentsService extends BaseSsoRecordsService<Agent, AgentsRepository> {

    public AgentsService(AgentsRepository repository) {
        super(Agent.class, repository);
    }

}

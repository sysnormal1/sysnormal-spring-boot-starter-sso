package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agents;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Agent;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.AgentsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AgentsService extends BaseSsoRecordsService<Agent, AgentsRepository> {

    public AgentsService(AgentsRepository repository) {
        super(Agent.class, repository);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.agents;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.Agent;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AgentsService extends BaseSsoRecordsService<Agent, AgentsRepository> {

    public AgentsService(AgentsRepository repository) {
        super(Agent.class, repository);
    }

}

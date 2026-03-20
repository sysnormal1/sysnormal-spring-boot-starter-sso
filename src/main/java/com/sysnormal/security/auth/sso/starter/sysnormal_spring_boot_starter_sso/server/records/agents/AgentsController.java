package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.agents;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agents.AgentsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/agents")
public class AgentsController extends BaseRecordsController<AgentsService> {

    public AgentsController(AgentsService service) {
        super(service);
    }

}

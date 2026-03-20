package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.agentsXAccessProfilesXSystems;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agentsXAccessProfilesXSystems.AgentsXAccessProfilesXSystemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/agents_x_access_profiles_x_systems")
public class AgentsXAccessProfilesXSystemsController extends BaseRecordsController<AgentsXAccessProfilesXSystemsService> {

    public AgentsXAccessProfilesXSystemsController(AgentsXAccessProfilesXSystemsService service) {
        super(service);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.agentsXAccessProfilesXDomains;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agentsXAccessProfilesXDomains.AgentsXAccessProfilesXDomainsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/agents_x_access_profiles_x_domains")
public class AgentsXAccessProfilesXDomainsController extends BaseRecordsController<AgentsXAccessProfilesXDomainsService> {

    public AgentsXAccessProfilesXDomainsController(AgentsXAccessProfilesXDomainsService service) {
        super(service);
    }

}

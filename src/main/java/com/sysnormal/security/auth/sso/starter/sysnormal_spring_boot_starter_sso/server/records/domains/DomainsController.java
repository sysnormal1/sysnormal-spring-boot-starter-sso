package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.domains;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domains.DomainsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/domains")
public class DomainsController extends BaseRecordsController<DomainsService> {

    public DomainsController(DomainsService service) {
        super(service);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.domainSides;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domainSides.DomainSidesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/domain_sides")
public class DomainSidesController extends BaseRecordsController<DomainSidesService> {

    public DomainSidesController(DomainSidesService service) {
        super(service);
    }
}

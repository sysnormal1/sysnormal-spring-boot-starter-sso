package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.domainPlatforms;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domainPlatforms.DomainPlatformsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/domain_platforms")
public class DomainPlatformsController extends BaseRecordsController<DomainPlatformsService> {

    public DomainPlatformsController(DomainPlatformsService service) {
        super(service);
    }
}

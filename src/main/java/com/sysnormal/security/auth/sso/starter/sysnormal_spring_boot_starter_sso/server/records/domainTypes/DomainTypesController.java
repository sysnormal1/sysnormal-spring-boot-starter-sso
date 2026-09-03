package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.domainTypes;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domainTypes.DomainTypesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/domain_types")
public class DomainTypesController extends BaseRecordsController<DomainTypesService> {

    public DomainTypesController(DomainTypesService service) {
        super(service);
    }

}

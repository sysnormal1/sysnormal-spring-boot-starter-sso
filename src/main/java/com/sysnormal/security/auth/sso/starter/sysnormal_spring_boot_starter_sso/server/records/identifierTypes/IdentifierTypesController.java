package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.identifierTypes;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.identifierTypes.IdentifierTypesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/identifier_types")
public class IdentifierTypesController extends BaseRecordsController<IdentifierTypesService> {

    public IdentifierTypesController(IdentifierTypesService service) {
        super(service);
    }

}

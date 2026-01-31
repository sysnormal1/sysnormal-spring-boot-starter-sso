package com.sysnormal.starters.security.sso.sso_starter.server.records.identifierTypes;

import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.identifierTypes.IdentifierTypesService;
import com.sysnormal.starters.security.sso.sso_starter.services.records.systems.SystemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/identifier_types")
public class IdentifierTypesController extends BaseRecordsController<IdentifierTypesService> {

    public IdentifierTypesController(IdentifierTypesService service) {
        super(service);
    }

}

package com.sysnormal.starters.security.sso.sso_starter.server.records.resourceTypes;

import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.resourceTypes.ResourceTypesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/resource_types")
public class ResourceTypesController extends BaseRecordsController<ResourceTypesService> {

    public ResourceTypesController(ResourceTypesService service) {
        super(service);
    }

}

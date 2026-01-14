package com.sysnormal.starters.security.sso.sso_starter.server.records.resources;

import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.resources.ResourcesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/resources")
public class ResourcesController extends BaseRecordsController<ResourcesService> {

    public ResourcesController(ResourcesService service) {
        super(service);
    }

}

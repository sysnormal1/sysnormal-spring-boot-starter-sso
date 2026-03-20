package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.server.records.resourceConfigurations;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.resourceConfigurations.ResourceConfigurationsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/resource_configurations")
public class ResourceConfigurationsController extends BaseRecordsController<ResourceConfigurationsService> {

    public ResourceConfigurationsController(ResourceConfigurationsService service) {
        super(service);
    }

}

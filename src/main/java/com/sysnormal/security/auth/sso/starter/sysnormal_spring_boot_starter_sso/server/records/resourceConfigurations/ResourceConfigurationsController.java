package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.resourceConfigurations;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.resourceConfigurations.ResourceConfigurationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/records/resource_configurations")
public class  ResourceConfigurationsController extends BaseRecordsController<ResourceConfigurationsService> {

    public ResourceConfigurationsController(ResourceConfigurationsService service) {
        super(service);
    }

    @PostMapping("/get_by_resource_name")
    public ResponseEntity<DefaultDataSwap> getByResourceName(
            @RequestBody(required = false) JsonNode body
    ) {
        DefaultDataSwap result = service.getByResourceName(body.get("resourceName").asString());
        return ResponseUtils.sendDefaultDataSwapResponse(result);
    }
}

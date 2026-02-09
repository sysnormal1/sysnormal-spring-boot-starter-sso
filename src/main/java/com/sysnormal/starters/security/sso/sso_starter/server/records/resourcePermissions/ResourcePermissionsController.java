package com.sysnormal.starters.security.sso.sso_starter.server.records.resourcePermissions;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.resourcePermissions.ResourcePermissionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/records/resource_permissions")
public class ResourcePermissionsController extends BaseRecordsController<ResourcePermissionsService> {
    private static final Logger logger = LoggerFactory.getLogger(ResourcePermissionsController.class);

    public ResourcePermissionsController(ResourcePermissionsService service) {
        super(service);
    }

    @PatchMapping("/update_permissions")
    public ResponseEntity<DefaultDataSwap> updatePermissions(@RequestBody(required = false) JsonNode body) {
        DefaultDataSwap result = service.updatePermissions(body);
        return result.sendHttpResponse();
    }

}

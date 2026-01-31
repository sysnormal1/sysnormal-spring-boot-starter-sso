package com.sysnormal.starters.security.sso.sso_starter.server.records.resources;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.resources.ResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/records/resources")
public class ResourcesController extends BaseRecordsController<ResourcesService> {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);

    public ResourcesController(ResourcesService service) {
        super(service);
    }

    @PostMapping("/get_with_permissions")
    public ResponseEntity<DefaultDataSwap> getWithPermissions(@RequestBody(required = false) JsonNode body) {
        DefaultDataSwap result = service.getWithPermissions(body);
        return result.sendHttpResponse();
    }
}

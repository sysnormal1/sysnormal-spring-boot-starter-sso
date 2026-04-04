package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.resources;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.auth.AuthenticationService;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.resources.ResourcesService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/records/resources")
public class ResourcesController extends BaseRecordsController<ResourcesService> {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    AuthenticationService authenticationService;

    public ResourcesController(ResourcesService service) {
        super(service);
    }

    @PostMapping("/get_alloweds")
    public ResponseEntity<DefaultDataSwap> getAlloweds(
            @RequestBody(required = false) JsonNode body
    ) {
        DefaultDataSwap result = new  DefaultDataSwap();
        try {
            if (body != null/* && body.has("token")*/) {
                //String token = body.get("token").asText();

                Claims claims = authenticationService.getAuthenticatedClaims();//jwtService.getClaims(token);
                Long agentId = authenticationService.getAuthenticatedAgentId();//claims.get("agentId", Long.class);

                if (agentId == null) {
                    //agentId = JsonUtils.get(queryParams,"agentId", JsonNode::asLong).orElse(null);
                    throw new Exception("missing authenticated agent id");
                }

                Long systemId = claims.get("systemId", Long.class);
                Long accessProfileId = claims.get("accessProfileId", Long.class);

                // se body vier null, cria objeto vazio
                ObjectNode objectBody = (body != null && body.isObject())
                        ? (ObjectNode) body
                        : objectMapper.createObjectNode();

                if (!objectBody.has("queryParams")) {
                    objectBody.putObject("queryParams");
                }

                ObjectNode queryParams = (ObjectNode) objectBody.get("queryParams");
                queryParams.put("agentId",agentId);
                if (systemId != null) queryParams.put("systemId",systemId);
                if (accessProfileId != null) queryParams.put("accessProfileId",accessProfileId);
                objectBody.set("queryParams", queryParams);
                result = service.getAlloweds(objectBody);
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.success = false;
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        return ResponseUtils.sendDefaultDataSwapResponse(result);
    }

    @PostMapping("/get_resource_permissions")
    public ResponseEntity<DefaultDataSwap> getResourcePermissions(
            @RequestBody(required = false) JsonNode body
    ) {
        DefaultDataSwap result = service.getResourcePermissions(body);
        return ResponseUtils.sendDefaultDataSwapResponse(result);
    }

    @PostMapping("/get_with_permissions")
    public ResponseEntity<DefaultDataSwap> getWithPermissions(
            @RequestBody(required = false) JsonNode body
    ) {
        DefaultDataSwap result = service.getWithPermissions(body);
        return ResponseUtils.sendDefaultDataSwapResponse(result);
    }

}

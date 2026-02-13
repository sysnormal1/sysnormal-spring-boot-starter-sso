package com.sysnormal.starters.security.sso.sso_starter.server.records.resources;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.jwt.JwtService;
import com.sysnormal.starters.security.sso.sso_starter.services.records.resources.ResourcesService;
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
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/records/resources")
public class ResourcesController extends BaseRecordsController<ResourcesService> {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtService jwtService;

    public ResourcesController(ResourcesService service) {
        super(service);
    }

    @PostMapping("/get_alloweds")
    public ResponseEntity<DefaultDataSwap> getAlloweds(
            @RequestBody(required = false) JsonNode body
    ) {
        DefaultDataSwap result = new  DefaultDataSwap();
        try {
            if (body != null && body.has("token")) {
                String token = body.get("token").asText();

                Claims claims = jwtService.getClaims(token);
                Long agentId = claims.get("agentId", Long.class);
                Long systemId = claims.get("systemId", Long.class);
                Long accessProfileId = claims.get("accessProfileId", Long.class);


                // se body vier null, cria objeto vazio
                ObjectNode objectBody = (body != null && body.isObject())
                        ? (ObjectNode) body
                        : objectMapper.createObjectNode();

                // agentIds
                ArrayNode agentIds = objectMapper.createArrayNode();
                agentIds.add(agentId);
                objectBody.set("agentIds", agentIds);

                // systemIds
                ArrayNode systemIds = objectMapper.createArrayNode();
                systemIds.add(systemId);
                objectBody.set("systemIds", systemIds);

                // accessProfileIds (só se existir)
                if (accessProfileId != null) {
                    ArrayNode accessProfileIds = objectMapper.createArrayNode();
                    accessProfileIds.add(accessProfileId);
                    objectBody.set("accessProfileIds", accessProfileIds);
                }

                result = service.getAlloweds(objectBody);
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.success = false;
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        return result.sendHttpResponse();
    }

    @PostMapping("/get_with_permissions")
    public ResponseEntity<DefaultDataSwap> getWithPermissions(
            @RequestBody(required = false) JsonNode body
    ) {
        DefaultDataSwap result = service.getWithPermissions(body);
        return result.sendHttpResponse();
    }

}

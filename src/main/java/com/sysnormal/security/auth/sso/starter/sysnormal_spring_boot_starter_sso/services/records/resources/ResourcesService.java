package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.resources;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.core.utils_core.JsonUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Resource;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.ResourcesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.auth.AuthenticationService;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Service
public class ResourcesService extends BaseSsoRecordsService<Resource, ResourcesRepository> {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesService.class);

    @Autowired
    AuthenticationService authenticationService;

    public ResourcesService(ResourcesRepository repository) {
        super(Resource.class, repository);
    }

    public DefaultDataSwap getAlloweds(JsonNode params) {
        DefaultDataSwap result =  new DefaultDataSwap();
        try {
            logger.debug("getAlloweds with params {}",params);
            JsonNode queryParams = params.path("queryParams");
            Long systemId = JsonUtils.get(queryParams,"systemId", JsonNode::asLong).orElse(null);
            Long resourceTypeId = JsonUtils.get(queryParams,"resourceTypeId", JsonNode::asLong).orElse(null);
            Long accessProfileId = JsonUtils.get(queryParams,"accessProfileId", JsonNode::asLong).orElse(null);
            Long agentId = JsonUtils.get(queryParams,"agentId", JsonNode::asLong).orElse(null);
            Byte allowedAccess = queryParams.has("allowedAccess") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedAccess")) : null;
            Byte allowedView = queryParams.has("allowedView") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedView")) : null;
            Byte allowedCreate = queryParams.has("allowedCreate") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedCreate")) : null;
            Byte allowedChange = queryParams.has("allowedChange") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedChange")) : null;
            Byte allowedDelete = queryParams.has("allowedDelete") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedDelete")) : null;

            result = getAlloweds(systemId,
                    resourceTypeId,
                    accessProfileId,
                    agentId,
                    allowedAccess,
                    allowedView,
                    allowedCreate,
                    allowedChange,
                    allowedDelete
            );
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    };

    public DefaultDataSwap getAlloweds(
            Long systemId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete
    ){
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "get");
        logger.debug("getAlloweds with systemId {}, resourceTypeId {}, accessProfileId {}, agentId {}",systemId, resourceTypeId, accessProfileId, agentId);
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            result.data = repository.findAlloweds(
                systemId,
                resourceTypeId,
                accessProfileId,
                agentId,
                allowedAccess,
                allowedView,
                allowedCreate,
                allowedChange,
                allowedDelete
            );
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "get");
        return result;
    }

    public DefaultDataSwap getResourcePermissions(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            JsonNode queryParams = params.path("queryParams");

            Claims claims = authenticationService.getAuthenticatedClaims();//jwtService.getClaims(token);
            Long agentId = authenticationService.getAuthenticatedAgentId();//claims.get("agentId", Long.class);
            Long systemId = claims.get("systemId", Long.class);
            Long accessProfileId = claims.get("accessProfileId", Long.class);

            if (agentId == null) {
                //agentId = JsonUtils.get(queryParams,"agentId", JsonNode::asLong).orElse(null);
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                throw new Exception("missing authenticated agent id");
            }
            if (accessProfileId == null) {
                accessProfileId = JsonUtils.get(queryParams,"accessProfileId", JsonNode::asLong).orElse(null);
            }
            if (systemId == null) {
                systemId = JsonUtils.get(queryParams,"systemId", JsonNode::asLong).orElse(null);
            }
            Long resourceTypeId = JsonUtils.get(queryParams,"resourceTypeId", JsonNode::asLong).orElse(null);
            List<String> resourcePaths = JsonUtils.jsonArrayToList(queryParams.path("resourcePaths"), JsonNode::asString);
            result.data = repository.findResourcePermissions(systemId, resourceTypeId, accessProfileId, agentId, resourcePaths, JoinType.INNER);
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    public DefaultDataSwap getWithPermissions(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            JsonNode queryParams = params.path("queryParams");
            List<Long> agentIds = JsonUtils.jsonArrayToList(queryParams.path("agentIds"), JsonNode::asLong);
            List<Long> systemIds = JsonUtils.jsonArrayToList(queryParams.path("systemIds"), JsonNode::asLong);
            List<Long> accessProfileIds = JsonUtils.jsonArrayToList(queryParams.path("accessProfileIds"), JsonNode::asLong);
            List<Long> resourceTypeIds = JsonUtils.jsonArrayToList(queryParams.path("resourceTypeIds"), JsonNode::asLong);
            List<String> resourcePaths = JsonUtils.jsonArrayToList(queryParams.path("resourcePaths"), JsonNode::asString);
            result.data = repository.findResourcePermissions(systemIds, resourceTypeIds, accessProfileIds, agentIds, resourcePaths, JoinType.LEFT);
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

}

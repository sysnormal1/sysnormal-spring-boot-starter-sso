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
            Long domainId = JsonUtils.get(queryParams,"domainId", JsonNode::asLong).orElse(null);
            Long resourceTypeId = JsonUtils.get(queryParams,"resourceTypeId", JsonNode::asLong).orElse(null);
            Long accessProfileId = JsonUtils.get(queryParams,"accessProfileId", JsonNode::asLong).orElse(null);
            Long agentId = JsonUtils.get(queryParams,"agentId", JsonNode::asLong).orElse(null);
            Byte allowedAccess = queryParams.has("allowedAccess") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedAccess")) : null;
            Byte allowedView = queryParams.has("allowedView") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedView")) : null;
            Byte allowedCreate = queryParams.has("allowedCreate") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedCreate")) : null;
            Byte allowedChange = queryParams.has("allowedChange") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedChange")) : null;
            Byte allowedDelete = queryParams.has("allowedDelete") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedDelete")) : null;
            boolean includeDeleted = JsonUtils.jsonNodeTo01(queryParams.path("includeDeleted")) == 1;
            boolean includeExpired = JsonUtils.jsonNodeTo01(queryParams.path("includeExpired")) == 1;

            result = getAlloweds(domainId,
                    resourceTypeId,
                    accessProfileId,
                    agentId,
                    allowedAccess,
                    allowedView,
                    allowedCreate,
                    allowedChange,
                    allowedDelete,
                    includeDeleted,
                    includeExpired
            );
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    };

    public DefaultDataSwap getAlloweds(
            Long domainId,
            Long resourceTypeId,
            Long accessProfileId,
            Long agentId,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete,
            boolean includeDeleted,
            boolean includeExpired
    ){
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "get");
        logger.debug("getAlloweds with domainId {}, resourceTypeId {}, accessProfileId {}, agentId {}",domainId, resourceTypeId, accessProfileId, agentId);
        logger.debug("getAlloweds with includeDeleted {}, includeExpired {}", includeDeleted, includeExpired);
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            //never widen to all domains when the domain is not informed: it would leak resources of other domains
            if (domainId == null) {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                throw new Exception("missing domainId: it is required to query allowed resources");
            }
            result.data = repository.findAlloweds(
                domainId,
                resourceTypeId,
                accessProfileId,
                agentId,
                allowedAccess,
                allowedView,
                allowedCreate,
                allowedChange,
                allowedDelete,
                includeDeleted,
                includeExpired
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
            Long domainId = claims.get("domainId", Long.class);
            Long accessProfileId = claims.get("accessProfileId", Long.class);

            if (agentId == null) {
                //agentId = JsonUtils.get(queryParams,"agentId", JsonNode::asLong).orElse(null);
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                throw new Exception("missing authenticated agent id");
            }
            if (accessProfileId == null) {
                accessProfileId = JsonUtils.get(queryParams,"accessProfileId", JsonNode::asLong).orElse(null);
            }
            if (domainId == null) {
                domainId = JsonUtils.get(queryParams,"domainId", JsonNode::asLong).orElse(null);
            }
            //neither the token nor the request informed the domain: refuse instead of returning every domain
            if (domainId == null) {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                throw new Exception("missing domainId: it is required to query resource permissions");
            }
            Long resourceTypeId = JsonUtils.get(queryParams,"resourceTypeId", JsonNode::asLong).orElse(null);
            List<String> resourcePaths = JsonUtils.jsonArrayToList(queryParams.path("resourcePaths"), JsonNode::asString);
            boolean includeDeleted = JsonUtils.jsonNodeTo01(queryParams.path("includeDeleted")) == 1;
            boolean includeExpired = JsonUtils.jsonNodeTo01(queryParams.path("includeExpired")) == 1;
            result.data = repository.findResourcePermissions(domainId, resourceTypeId, accessProfileId, agentId, resourcePaths, JoinType.INNER, includeDeleted, includeExpired);
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
            List<Long> domainIds = JsonUtils.jsonArrayToList(queryParams.path("domainIds"), JsonNode::asLong);
            //without the domain this query returns the whole catalog, of every domain: refuse it
            if (domainIds == null || domainIds.isEmpty()) {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                throw new Exception("missing domainIds: it is required to query resources with permissions");
            }
            List<Long> accessProfileIds = JsonUtils.jsonArrayToList(queryParams.path("accessProfileIds"), JsonNode::asLong);
            List<Long> resourceTypeIds = JsonUtils.jsonArrayToList(queryParams.path("resourceTypeIds"), JsonNode::asLong);
            List<String> resourcePaths = JsonUtils.jsonArrayToList(queryParams.path("resourcePaths"), JsonNode::asString);
            boolean includeDeleted = JsonUtils.jsonNodeTo01(queryParams.path("includeDeleted")) == 1;
            boolean includeExpired = JsonUtils.jsonNodeTo01(queryParams.path("includeExpired")) == 1;
            result.data = repository.findResourcePermissions(domainIds, resourceTypeIds, accessProfileIds, agentIds, resourcePaths, JoinType.LEFT, includeDeleted, includeExpired);
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

}

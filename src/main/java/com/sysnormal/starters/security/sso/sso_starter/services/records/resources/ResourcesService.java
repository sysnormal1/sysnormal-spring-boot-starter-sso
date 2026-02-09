package com.sysnormal.starters.security.sso.sso_starter.services.records.resources;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.libs.utils.JsonUtils;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Resource;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.ResourcesRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Service
public class ResourcesService extends BaseSsoRecordsService<Resource, ResourcesRepository> {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesService.class);
    public ResourcesService(ResourcesRepository repository) {
        super(Resource.class, repository);
    }

    public DefaultDataSwap getAlloweds(JsonNode params) {
        DefaultDataSwap result =  new DefaultDataSwap();
        try {
            JsonNode queryParams = params.path("queryParams");
            List<Long> systemIds = JsonUtils.jsonArrayToList(queryParams.path("systemIds"), JsonNode::asLong);
            List<Long> resourceTypeIds = JsonUtils.jsonArrayToList(queryParams.path("resourceTypeIds"), JsonNode::asLong);
            List<Long> accessProfileIds = JsonUtils.jsonArrayToList(queryParams.path("accessProfileIds"), JsonNode::asLong);
            List<Long> agentIds = JsonUtils.jsonArrayToList(queryParams.path("agentIds"), JsonNode::asLong);
            Byte allowedAccess = queryParams.has("allowedAccess") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedAccess")) : null;
            Byte allowedView = queryParams.has("allowedView") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedView")) : null;
            Byte allowedCreate = queryParams.has("allowedCreate") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedCreate")) : null;
            Byte allowedChange = queryParams.has("allowedChange") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedChange")) : null;
            Byte allowedDelete = queryParams.has("allowedDelete") ? JsonUtils.jsonNodeTo01(queryParams.path("allowedDelete")) : null;

            result = getAlloweds(systemIds,
                    resourceTypeIds,
                    accessProfileIds,
                    agentIds,
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
            List<Long> systemIds,
            List<Long> resourceTypeIds,
            List<Long> accessProfileIds,
            List<Long> agentIds,
            Byte allowedAccess,
            Byte allowedView,
            Byte allowedCreate,
            Byte allowedChange,
            Byte allowedDelete
    ){
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "get");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            result.data = repository.findAlloweds(
                systemIds,
                resourceTypeIds,
                accessProfileIds,
                agentIds,
                allowedAccess,
                allowedView,
                allowedCreate,
                allowedChange,
                allowedDelete
            );
            result.success = true;
        } catch (Exception e) {
            e.printStackTrace();
            result.setException(e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "get");
        return result;
    }

    public DefaultDataSwap getWithPermissions(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            JsonNode queryParams = params.path("queryParams");
            List<Long> systemIds = JsonUtils.jsonArrayToList(queryParams.path("systemIds"), JsonNode::asLong);
            List<Long> resourceTypeIds = JsonUtils.jsonArrayToList(queryParams.path("resourceTypeIds"), JsonNode::asLong);
            List<Long> accessProfileIds = JsonUtils.jsonArrayToList(queryParams.path("accessProfileIds"), JsonNode::asLong);
            List<Long> agentIds = JsonUtils.jsonArrayToList(queryParams.path("agentIds"), JsonNode::asLong);
            List<String> resourcePaths = JsonUtils.jsonArrayToList(queryParams.path("resourcePaths"), JsonNode::asString);
            result.data = repository.findWithPermissions(systemIds, resourceTypeIds, accessProfileIds, agentIds, resourcePaths);
            result.success = true;
        } catch (Exception e) {
            e.printStackTrace();
            result.setException(e);
        }
        return result;
    }

}

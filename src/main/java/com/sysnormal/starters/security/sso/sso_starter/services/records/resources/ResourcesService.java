package com.sysnormal.starters.security.sso.sso_starter.services.records.resources;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.libs.utils.database.DatabaseUtils;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Resource;
import com.sysnormal.starters.security.sso.sso_starter.database.projections.IdAndNameProjection;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.ResourcesRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourcesService extends BaseSsoRecordsService<Resource, ResourcesRepository> {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesService.class);
    public ResourcesService(ResourcesRepository repository) {
        super(Resource.class, repository);
    }


    public DefaultDataSwap getWithPermissions(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            JsonNode queryParams = params.path("queryParams");
            List<Long> systemIds = null;
            List<Long> resourceTypeIds = null;
            List<Long> accessProfileIds = null;
            List<Long> agentIds = null;
            if (queryParams != null && !queryParams.isEmpty()) {
                logger.debug("queryParams: " + queryParams.toString());
                JsonNode systemIdsNode = queryParams.path("systemIds");
                if (systemIdsNode != null && !systemIdsNode.isEmpty() && systemIdsNode.isArray()) {
                    systemIds =  new ArrayList<Long>();
                    logger.debug("systemIdsNode: " + systemIdsNode.toString());
                    for (int i = 0; i < systemIdsNode.size(); i++) {
                        systemIds.add(systemIdsNode.get(i).asLong());
                    }
                }
                JsonNode resourceTypeIdsNode = queryParams.path("resourceTypeIds");
                if (resourceTypeIdsNode != null && !resourceTypeIdsNode.isEmpty() && resourceTypeIdsNode.isArray()) {
                    resourceTypeIds =  new ArrayList<Long>();
                    logger.debug("resourceTypeIdsNode: " + resourceTypeIdsNode.toString());
                    for (int i = 0; i < resourceTypeIdsNode.size(); i++) {
                        resourceTypeIds.add(resourceTypeIdsNode.get(i).asLong());
                    }
                }
                JsonNode accessProfileIdsNode = queryParams.path("accessProfileIds");
                if (accessProfileIdsNode != null && !accessProfileIdsNode.isEmpty() && accessProfileIdsNode.isArray()) {
                    accessProfileIds =  new ArrayList<Long>();
                    logger.debug("accessProfileIdsNode: " + accessProfileIdsNode.toString());
                    for (int i = 0; i < accessProfileIdsNode.size(); i++) {
                        accessProfileIds.add(accessProfileIdsNode.get(i).asLong());
                    }
                }
                JsonNode agentIdsNode = queryParams.path("agentIds");
                if (agentIdsNode != null && !agentIdsNode.isEmpty() && agentIdsNode.isArray()) {
                    agentIds =  new ArrayList<Long>();
                    logger.debug("agentIdsNode: " + agentIdsNode.toString());
                    for (int i = 0; i < agentIdsNode.size(); i++) {
                        agentIds.add(agentIdsNode.get(i).asLong());
                    }
                }
            }
            result.data = repository.findWithPermissions(systemIds, resourceTypeIds, accessProfileIds, agentIds);
            result.success = true;
        } catch (Throwable e) {
            e.printStackTrace();
            //result.setException(e);
        }
        return result;
    }
}

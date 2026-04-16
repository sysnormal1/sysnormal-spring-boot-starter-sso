package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.resourcePermissions;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.core.utils_core.JsonUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourcePermission;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.ResourcePermissionsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

@Service
public class ResourcePermissionsService extends BaseSsoRecordsService<ResourcePermission, ResourcePermissionsRepository> {

    private static final Logger logger = LoggerFactory.getLogger(ResourcePermissionsService.class);
    public ResourcePermissionsService(ResourcePermissionsRepository repository) {
        super(ResourcePermission.class, repository);
    }

    public DefaultDataSwap updatePermissions(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            JsonNode data = params.path("data");
            if (data != null && !data.isNull() && !data.isEmpty() && data.isArray() && data.size() > 0) {
                List<JsonNode> dataList = JsonUtils.jsonArrayToList(data, n->n);
                for (JsonNode jsonNode : dataList) {
                    Long resourcePermissionId = JsonUtils.get(jsonNode,"id", JsonNode::asLong).orElse(null);
                    if (resourcePermissionId != null) {
                        Optional<ResourcePermission> optionalResourcePermission = repository.findById(resourcePermissionId);
                        if (optionalResourcePermission.isPresent()) {
                            if (jsonNode.has("allowedAccess")) {
                                optionalResourcePermission.get().setAllowedAccess(JsonUtils.jsonNodeTo01(jsonNode.get("allowedAccess")));
                            }
                            if (jsonNode.has("allowedView")) {
                                optionalResourcePermission.get().setAllowedView(JsonUtils.jsonNodeTo01(jsonNode.get("allowedView")));
                            }
                            if (jsonNode.has("allowedCreate")) {
                                optionalResourcePermission.get().setAllowedCreate(JsonUtils.jsonNodeTo01(jsonNode.get("allowedCreate")));
                            }
                            if (jsonNode.has("allowedChange")) {
                                optionalResourcePermission.get().setAllowedChange(JsonUtils.jsonNodeTo01(jsonNode.get("allowedChange")));
                            }
                            if (jsonNode.has("allowedDelete")) {
                                optionalResourcePermission.get().setAllowedDelete(JsonUtils.jsonNodeTo01(jsonNode.get("allowedDelete")));
                            }
                            repository.save(optionalResourcePermission.get());
                        } else {
                            result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                            throw new Exception("resource permission not found");
                        }
                    } else {
                        ResourcePermission resourcePermission = new ResourcePermission();
                        resourcePermission.setResourceId(JsonUtils.get(jsonNode,"resourceId", JsonNode::asLong).orElse(null));
                        resourcePermission.setAccessProfileId(JsonUtils.get(jsonNode,"accessProfileId", JsonNode::asLong).orElse(null));
                        resourcePermission.setAgentId(JsonUtils.get(jsonNode,"agentId", JsonNode::asLong).orElse(null));
                        if (jsonNode.has("allowedAccess")) {
                            resourcePermission.setAllowedAccess(JsonUtils.jsonNodeTo01(jsonNode.get("allowedAccess")));
                        }
                        if (jsonNode.has("allowedView")) {
                            resourcePermission.setAllowedView(JsonUtils.jsonNodeTo01(jsonNode.get("allowedView")));
                        }
                        if (jsonNode.has("allowedCreate")) {
                            resourcePermission.setAllowedCreate(JsonUtils.jsonNodeTo01(jsonNode.get("allowedCreate")));
                        }
                        if (jsonNode.has("allowedChange")) {
                            resourcePermission.setAllowedChange(JsonUtils.jsonNodeTo01(jsonNode.get("allowedChange")));
                        }
                        if (jsonNode.has("allowedDelete")) {
                            resourcePermission.setAllowedDelete(JsonUtils.jsonNodeTo01(jsonNode.get("allowedDelete")));
                        }
                        repository.save(resourcePermission);
                    }
                }
                result.data = dataList;
                result.success = true;
            } else {
                result.success = false;
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}

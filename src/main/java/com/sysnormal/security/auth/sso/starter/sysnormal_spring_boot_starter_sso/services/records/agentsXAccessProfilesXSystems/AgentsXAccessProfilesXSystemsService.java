package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agentsXAccessProfilesXSystems;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.core.utils_core.JsonUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AgentXAccessProfileXSystem;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.System;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.AgentsXAccessProfilesXSystemsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.SystemsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.auth.AuthenticationService;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Optional;

@Service
public class AgentsXAccessProfilesXSystemsService extends BaseSsoRecordsService<AgentXAccessProfileXSystem, AgentsXAccessProfilesXSystemsRepository> {

    private final SystemsRepository systemsRepository;
    private final ObjectMapper objectMapper;
    private final AuthenticationService authenticationService;

    public AgentsXAccessProfilesXSystemsService(
            AgentsXAccessProfilesXSystemsRepository repository,
            SystemsRepository systemsRepository,
            ObjectMapper objectMapper,
            AuthenticationService authenticationService
    ) {
        super(AgentXAccessProfileXSystem.class, repository);
        this.systemsRepository = systemsRepository;
        this.objectMapper = objectMapper;
        this.authenticationService = authenticationService;
    }

    private ObjectNode getWhereNode(JsonNode params) {
        ObjectNode mutableParams = (ObjectNode) params;

        if (!mutableParams.has("queryParams")) {
            mutableParams.set("queryParams", objectMapper.createObjectNode());
        }
        ObjectNode queryParams = (ObjectNode) mutableParams.get("queryParams");

        if (!queryParams.has("where")) {
            queryParams.set("where", objectMapper.createObjectNode());
        }
        return (ObjectNode) queryParams.get("where");
    }

    @Override
    public DefaultDataSwap get(JsonNode params) {
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            JsonNode systemNameNode = params.path("systemName");
            if (systemNameNode != null && !systemNameNode.isMissingNode() && !systemNameNode.isNull()) {
                String systemName = systemNameNode.asText();
                Optional<System> system = systemsRepository.findByName(systemName);
                if (system.isPresent()) {
                    ObjectNode where = getWhereNode(params);
                    where.put("systemId", system.get().getId());
                }
            }

            Long agentId = authenticationService.getAuthenticatedAgentId();
            if (agentId != null) {
                ObjectNode where = getWhereNode(params);
                where.put("agentId", agentId);
            }

            result.data = repository.findAll(getSpecificationFromJsonNode(params));
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

}

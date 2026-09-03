package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.agentsXAccessProfilesXDomains;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.core.utils_core.JsonUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AccessProfile;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AgentXAccessProfileXDomain;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Domain;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.AgentsXAccessProfilesXDomainsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.DomainsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.auth.AuthenticationService;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt.JwtSsoService;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Optional;

@Service
public class AgentsXAccessProfilesXDomainsService extends BaseSsoRecordsService<AgentXAccessProfileXDomain, AgentsXAccessProfilesXDomainsRepository> {

    private static final Logger logger = LoggerFactory.getLogger(AgentsXAccessProfilesXDomainsService.class);

    private final DomainsRepository domainsRepository;
    private final ObjectMapper objectMapper;
    private final AuthenticationService authenticationService;

    public AgentsXAccessProfilesXDomainsService(
            AgentsXAccessProfilesXDomainsRepository repository,
            DomainsRepository domainsRepository,
            ObjectMapper objectMapper,
            AuthenticationService authenticationService
    ) {
        super(AgentXAccessProfileXDomain.class, repository);
        this.domainsRepository = domainsRepository;
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

    private boolean isSystemAgent(Long agentId) {
        boolean result = repository.existsByAgentIdAndAccessProfileId(agentId, AccessProfile.SYSTEM_ID);
        logger.debug("agent {} is system agent: {}", agentId, result);
        return result;
    }

    @Override
    public DefaultDataSwap get(JsonNode params) {
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            logger.debug("params {}",params);
            JsonNode domainNameNode = params.path("domainName");
            logger.debug("domainNameNode {}",domainNameNode);
            if (domainNameNode != null && !domainNameNode.isMissingNode() && !domainNameNode.isNull()) {
                String domainName = domainNameNode.asText();
                logger.debug("domainName {}",domainName);
                Optional<Domain> domain = domainsRepository.findByName(domainName);
                logger.debug("domain {}",domain);
                if (domain.isPresent()) {
                    ObjectNode where = getWhereNode(params);
                    logger.debug("where {}",where);
                    where.put("domainId", domain.get().getId());
                    logger.debug("where {}",where);
                }
            }

            /*
             * by default an agent only sees its own links. an agent with the SYSTEM access profile
             * administers the sso, so it sees every link, of every agent and every domain.
             * the profile is resolved from the stored links, not from the token: the accessProfileId
             * claim is optional and is absent whenever the login did not inform it.
             */
            Long agentId = authenticationService.getAuthenticatedAgentId();
            logger.debug("agentId {}",agentId);
            if (agentId != null && !isSystemAgent(agentId)) {
                ObjectNode where = getWhereNode(params);
                logger.debug("where {}",where);
                where.put("agentId", agentId);
                logger.debug("where {}",where);
            }
            logger.debug("where {}",params);
            result.data = repository.findAll(getSpecificationFromJsonNode(params));
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

}

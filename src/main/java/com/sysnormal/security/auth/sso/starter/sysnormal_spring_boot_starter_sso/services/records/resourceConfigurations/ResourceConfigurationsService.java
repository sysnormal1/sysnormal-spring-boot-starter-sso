package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.resourceConfigurations;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.core.utils_core.JsonUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourceConfiguration;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.ResourceConfigurationsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.criteria.JoinType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Service
public class ResourceConfigurationsService extends BaseSsoRecordsService<ResourceConfiguration, ResourceConfigurationsRepository> {

    public ResourceConfigurationsService(ResourceConfigurationsRepository repository) {
        super(ResourceConfiguration.class, repository);
    }


    public DefaultDataSwap getByResourceName(String resourceName){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            result.data = repository.findByResourceName(resourceName);
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}

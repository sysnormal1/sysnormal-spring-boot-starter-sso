package com.sysnormal.security.auth.sso.starter.services.records.resourceConfigurations;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.ResourceConfiguration;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.ResourceConfigurationsRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourceConfigurationsService extends BaseSsoRecordsService<ResourceConfiguration, ResourceConfigurationsRepository> {

    public ResourceConfigurationsService(ResourceConfigurationsRepository repository) {
        super(ResourceConfiguration.class, repository);
    }

}

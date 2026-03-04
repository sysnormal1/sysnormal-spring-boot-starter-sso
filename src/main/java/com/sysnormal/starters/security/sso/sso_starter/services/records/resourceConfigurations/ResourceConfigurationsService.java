package com.sysnormal.starters.security.sso.sso_starter.services.records.resourceConfigurations;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.ResourceConfiguration;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.ResourceConfigurationsRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourceConfigurationsService extends BaseSsoRecordsService<ResourceConfiguration, ResourceConfigurationsRepository> {

    public ResourceConfigurationsService(ResourceConfigurationsRepository repository) {
        super(ResourceConfiguration.class, repository);
    }

}

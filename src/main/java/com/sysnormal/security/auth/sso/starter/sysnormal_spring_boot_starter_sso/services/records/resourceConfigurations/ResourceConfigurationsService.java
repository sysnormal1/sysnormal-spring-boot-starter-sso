package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.resourceConfigurations;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.ResourceConfiguration;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.ResourceConfigurationsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourceConfigurationsService extends BaseSsoRecordsService<ResourceConfiguration, ResourceConfigurationsRepository> {

    public ResourceConfigurationsService(ResourceConfigurationsRepository repository) {
        super(ResourceConfiguration.class, repository);
    }

}

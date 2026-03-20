package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.resourceConfigurations;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.ResourceConfiguration;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.ResourceConfigurationsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourceConfigurationsService extends BaseSsoRecordsService<ResourceConfiguration, ResourceConfigurationsRepository> {

    public ResourceConfigurationsService(ResourceConfigurationsRepository repository) {
        super(ResourceConfiguration.class, repository);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.resourceTypes;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.ResourceType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.ResourceTypesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypesService extends BaseSsoRecordsService<ResourceType, ResourceTypesRepository> {

    public ResourceTypesService(ResourceTypesRepository repository) {
        super(ResourceType.class, repository);
    }

}

package com.sysnormal.starters.security.sso.sso_starter.services.records.resourceTypes;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.ResourceType;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.ResourceTypesRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypesService extends BaseSsoRecordsService<ResourceType, ResourceTypesRepository> {

    public ResourceTypesService(ResourceTypesRepository repository) {
        super(ResourceType.class, repository);
    }

}

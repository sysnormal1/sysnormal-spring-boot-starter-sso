package com.sysnormal.starters.security.sso.sso_starter.services.records.resources;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Resource;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.ResourcesRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class ResourcesService extends BaseSsoRecordsService<Resource, ResourcesRepository> {

    public ResourcesService(ResourcesRepository repository) {
        super(Resource.class, repository);
    }

}

package com.sysnormal.starters.security.sso.sso_starter.services.records.systemPlatforms;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.SystemPlatform;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.SystemPlatformsRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemPlatformsService extends BaseSsoRecordsService<SystemPlatform, SystemPlatformsRepository> {

    public SystemPlatformsService(SystemPlatformsRepository repository) {
        super(SystemPlatform.class, repository);
    }

}

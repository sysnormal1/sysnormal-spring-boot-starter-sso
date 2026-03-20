package com.sysnormal.security.auth.sso.starter.services.records.systemPlatforms;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.SystemPlatformType;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.SystemPlatformsRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemPlatformsService extends BaseSsoRecordsService<SystemPlatformType, SystemPlatformsRepository> {

    public SystemPlatformsService(SystemPlatformsRepository repository) {
        super(SystemPlatformType.class, repository);
    }

}

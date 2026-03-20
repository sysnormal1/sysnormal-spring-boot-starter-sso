package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.systemPlatforms;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.SystemPlatformType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.SystemPlatformsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemPlatformsService extends BaseSsoRecordsService<SystemPlatformType, SystemPlatformsRepository> {

    public SystemPlatformsService(SystemPlatformsRepository repository) {
        super(SystemPlatformType.class, repository);
    }

}

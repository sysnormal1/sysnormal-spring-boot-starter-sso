package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.systemPlatforms;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.SystemPlatformType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.SystemPlatformsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemPlatformsService extends BaseSsoRecordsService<SystemPlatformType, SystemPlatformsRepository> {

    public SystemPlatformsService(SystemPlatformsRepository repository) {
        super(SystemPlatformType.class, repository);
    }

}

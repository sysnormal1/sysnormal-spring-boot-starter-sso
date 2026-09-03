package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domainPlatforms;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.DomainPlatformType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.DomainPlatformsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class DomainPlatformsService extends BaseSsoRecordsService<DomainPlatformType, DomainPlatformsRepository> {

    public DomainPlatformsService(DomainPlatformsRepository repository) {
        super(DomainPlatformType.class, repository);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domainSides;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.DomainSide;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.DomainSidesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class DomainSidesService extends BaseSsoRecordsService<DomainSide, DomainSidesRepository> {

    public DomainSidesService(DomainSidesRepository repository) {
        super(DomainSide.class, repository);
    }

}

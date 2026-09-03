package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domainTypes;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.DomainType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.DomainTypesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class DomainTypesService extends BaseSsoRecordsService<DomainType, DomainTypesRepository> {

    public DomainTypesService(DomainTypesRepository repository) {
        super(DomainType.class, repository);
    }

}

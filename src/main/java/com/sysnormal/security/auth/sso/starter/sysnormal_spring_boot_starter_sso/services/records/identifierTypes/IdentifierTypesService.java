package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.identifierTypes;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.IdentifierType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.IdentifierTypesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class IdentifierTypesService extends BaseSsoRecordsService<IdentifierType, IdentifierTypesRepository> {

    public IdentifierTypesService(IdentifierTypesRepository repository) {
        super(IdentifierType.class, repository);
    }

}

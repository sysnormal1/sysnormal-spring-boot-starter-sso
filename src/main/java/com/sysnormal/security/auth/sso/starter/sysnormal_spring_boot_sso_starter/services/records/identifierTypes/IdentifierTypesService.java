package com.sysnormal.security.auth.sso.starter.services.records.identifierTypes;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.IdentifierType;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.IdentifierTypesRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class IdentifierTypesService extends BaseSsoRecordsService<IdentifierType, IdentifierTypesRepository> {

    public IdentifierTypesService(IdentifierTypesRepository repository) {
        super(IdentifierType.class, repository);
    }

}

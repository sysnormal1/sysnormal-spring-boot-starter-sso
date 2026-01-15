package com.sysnormal.starters.security.sso.sso_starter.services.records.systems;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.System;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.SystemsRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemsService extends BaseSsoRecordsService<System, SystemsRepository> {

    public SystemsService(SystemsRepository repository) {
        super(System.class, repository);
    }

}

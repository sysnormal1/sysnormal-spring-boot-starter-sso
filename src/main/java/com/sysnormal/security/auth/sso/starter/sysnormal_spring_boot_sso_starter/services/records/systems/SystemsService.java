package com.sysnormal.security.auth.sso.starter.services.records.systems;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.System;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.SystemsRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemsService extends BaseSsoRecordsService<System, SystemsRepository> {

    public SystemsService(SystemsRepository repository) {
        super(System.class, repository);
    }

}

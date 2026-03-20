package com.sysnormal.security.auth.sso.starter.services.records.systemSides;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.SystemSide;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.SystemSidesRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemSidesService extends BaseSsoRecordsService<SystemSide, SystemSidesRepository> {

    public SystemSidesService(SystemSidesRepository repository) {
        super(SystemSide.class, repository);
    }

}

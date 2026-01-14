package com.sysnormal.starters.security.sso.sso_starter.services.records.systemSides;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.SystemSide;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.SystemSidesRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemSidesService extends BaseSsoRecordsService<SystemSide, SystemSidesRepository> {

    public SystemSidesService(SystemSidesRepository repository) {
        super(SystemSide.class, repository);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.systemSides;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.SystemSide;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.SystemSidesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class SystemSidesService extends BaseSsoRecordsService<SystemSide, SystemSidesRepository> {

    public SystemSidesService(SystemSidesRepository repository) {
        super(SystemSide.class, repository);
    }

}

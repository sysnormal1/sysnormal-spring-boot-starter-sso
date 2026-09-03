package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.domains;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.Domain;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.DomainsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class DomainsService extends BaseSsoRecordsService<Domain, DomainsRepository> {

    public DomainsService(DomainsRepository repository) {
        super(Domain.class, repository);
    }

}

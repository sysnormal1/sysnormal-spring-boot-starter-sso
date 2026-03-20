package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.accessProfiles;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.AccessProfile;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.AccessProfilesRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AccessProfilesService extends BaseSsoRecordsService<AccessProfile, AccessProfilesRepository> {

    public AccessProfilesService(AccessProfilesRepository repository) {
        super(AccessProfile.class, repository);
    }

}

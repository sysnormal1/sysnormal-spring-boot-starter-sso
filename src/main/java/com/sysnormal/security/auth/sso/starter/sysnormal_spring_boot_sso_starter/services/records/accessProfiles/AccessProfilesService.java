package com.sysnormal.security.auth.sso.starter.services.records.accessProfiles;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.AccessProfile;
import com.sysnormal.security.auth.sso.starter.database.repositories.sso.AccessProfilesRepository;
import com.sysnormal.security.auth.sso.starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AccessProfilesService extends BaseSsoRecordsService<AccessProfile, AccessProfilesRepository> {

    public AccessProfilesService(AccessProfilesRepository repository) {
        super(AccessProfile.class, repository);
    }

}

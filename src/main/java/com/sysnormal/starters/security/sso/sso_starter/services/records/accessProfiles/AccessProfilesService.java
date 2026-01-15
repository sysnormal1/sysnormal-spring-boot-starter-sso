package com.sysnormal.starters.security.sso.sso_starter.services.records.accessProfiles;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.AccessProfile;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.AccessProfilesRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
import org.springframework.stereotype.Service;

@Service
public class AccessProfilesService extends BaseSsoRecordsService<AccessProfile, AccessProfilesRepository> {

    public AccessProfilesService(AccessProfilesRepository repository) {
        super(AccessProfile.class, repository);
    }

}

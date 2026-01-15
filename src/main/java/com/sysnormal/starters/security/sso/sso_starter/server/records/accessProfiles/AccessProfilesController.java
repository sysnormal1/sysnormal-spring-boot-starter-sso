package com.sysnormal.starters.security.sso.sso_starter.server.records.accessProfiles;

import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.accessProfiles.AccessProfilesService;
import com.sysnormal.starters.security.sso.sso_starter.services.records.systems.SystemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/access_profiles")
public class AccessProfilesController extends BaseRecordsController<AccessProfilesService> {

    public AccessProfilesController(AccessProfilesService service) {
        super(service);
    }

}

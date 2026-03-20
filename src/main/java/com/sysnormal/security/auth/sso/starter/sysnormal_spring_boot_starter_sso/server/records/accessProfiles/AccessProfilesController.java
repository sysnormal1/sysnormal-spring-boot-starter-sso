package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.accessProfiles;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.accessProfiles.AccessProfilesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/access_profiles")
public class AccessProfilesController extends BaseRecordsController<AccessProfilesService> {

    public AccessProfilesController(AccessProfilesService service) {
        super(service);
    }

}

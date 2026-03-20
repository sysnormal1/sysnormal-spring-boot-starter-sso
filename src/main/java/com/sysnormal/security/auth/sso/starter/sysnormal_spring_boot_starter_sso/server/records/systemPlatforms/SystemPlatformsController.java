package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.systemPlatforms;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.systemPlatforms.SystemPlatformsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/system_platforms")
public class SystemPlatformsController extends BaseRecordsController<SystemPlatformsService> {

    public SystemPlatformsController(SystemPlatformsService service) {
        super(service);
    }
}

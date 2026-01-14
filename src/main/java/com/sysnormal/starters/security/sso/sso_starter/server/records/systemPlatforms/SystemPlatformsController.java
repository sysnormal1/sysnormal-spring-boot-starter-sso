package com.sysnormal.starters.security.sso.sso_starter.server.records.systemPlatforms;

import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.systemPlatforms.SystemPlatformsService;
import com.sysnormal.starters.security.sso.sso_starter.services.records.systems.SystemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/system_platforms")
public class SystemPlatformsController extends BaseRecordsController<SystemPlatformsService> {

    public SystemPlatformsController(SystemPlatformsService service) {
        super(service);
    }
}

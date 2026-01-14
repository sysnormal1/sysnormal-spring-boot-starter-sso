package com.sysnormal.starters.security.sso.sso_starter.server.records.systems;

import com.sysnormal.starters.security.sso.sso_starter.server.records.BaseRecordsController;
import com.sysnormal.starters.security.sso.sso_starter.services.records.systems.SystemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/systems")
public class SystemsController extends BaseRecordsController<SystemsService> {

    public SystemsController(SystemsService service) {
        super(service);
    }

}

package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.systems;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records.systems.SystemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/systems")
public class SystemsController extends BaseRecordsController<SystemsService> {

    public SystemsController(SystemsService service) {
        super(service);
    }

}

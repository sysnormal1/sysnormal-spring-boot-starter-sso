package com.sysnormal.security.auth.sso.starter.server.records.systemSides;

import com.sysnormal.security.auth.sso.starter.server.records.BaseRecordsController;
import com.sysnormal.security.auth.sso.starter.services.records.systemSides.SystemSidesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/system_sides")
public class SystemSidesController extends BaseRecordsController<SystemSidesService> {

    public SystemSidesController(SystemSidesService service) {
        super(service);
    }
}

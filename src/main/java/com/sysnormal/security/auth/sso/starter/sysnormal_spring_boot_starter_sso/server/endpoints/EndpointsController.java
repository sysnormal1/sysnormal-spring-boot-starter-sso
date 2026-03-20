package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.endpoints;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.endpoints.EndpointsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/endpoints")
public class EndpointsController {

    private static final Logger logger = LoggerFactory.getLogger(EndpointsController.class);

    @Autowired
    private EndpointsService endpointsService;

    @PostMapping("/get")
    public ResponseEntity<DefaultDataSwap> get(@RequestBody(required = false) JsonNode body) {
        logger.debug("requested endpoints/get {}", body);
        return ResponseUtils.sendDefaultDataSwapResponse(endpointsService.get(body));
    }
}

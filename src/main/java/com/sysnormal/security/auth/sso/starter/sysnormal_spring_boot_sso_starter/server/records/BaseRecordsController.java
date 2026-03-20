package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.server.records;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.BaseSsoEntity;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.BaseSsoRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.records.BaseSsoRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

public abstract class BaseRecordsController<S extends BaseSsoRecordsService<? extends BaseSsoEntity<?>,? extends BaseSsoRepository<?, Long>>> {

    private static final Logger logger = LoggerFactory.getLogger(BaseRecordsController.class);

    protected final S service;

    protected BaseRecordsController(S service) {
        this.service = service;
    }


    @RequestMapping(
            path = { "/", "/get", "/get/" },
            method = { RequestMethod.GET, RequestMethod.POST }
    )
    public ResponseEntity<DefaultDataSwap> get(@RequestBody(required = false) JsonNode body) {
        //@todo implement generic middleware to check if this resource (detect requested endpoint) is allowed to this user (resource and resource_permission tables store theses permissions)

        return ResponseUtils.sendDefaultDataSwapResponse(service.get(body));
    }

    @RequestMapping(
            path = { "/{id}", "/get/{id}", "/get/{id}/" },
            method = { RequestMethod.GET, RequestMethod.POST }
    )
    public ResponseEntity<DefaultDataSwap> getById(@PathVariable("id") Long id, @RequestBody(required = false) JsonNode body) {
        return ResponseUtils.sendDefaultDataSwapResponse(service.getById(id, body));
    }

    @PutMapping("")
    public ResponseEntity<DefaultDataSwap> put(@RequestBody(required = false) JsonNode body) {
        return ResponseUtils.sendDefaultDataSwapResponse(service.put(body));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DefaultDataSwap> patch(@PathVariable("id") Long id, @RequestBody(required = false) JsonNode body) {
        return ResponseUtils.sendDefaultDataSwapResponse(service.patch(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultDataSwap> deleteById(@PathVariable("id") Long id, @RequestBody(required = false) JsonNode body) {
        return ResponseUtils.sendDefaultDataSwapResponse(service.deleteById(id, body));
    }

    @DeleteMapping("")
    public ResponseEntity<DefaultDataSwap> delete(@RequestBody(required = false) JsonNode body) {
        return ResponseUtils.sendDefaultDataSwapResponse(service.delete(body));
    }
}

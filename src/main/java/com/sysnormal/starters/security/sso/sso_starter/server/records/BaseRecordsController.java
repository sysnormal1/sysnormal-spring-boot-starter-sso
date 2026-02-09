package com.sysnormal.starters.security.sso.sso_starter.server.records;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.BaseSsoEntity;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.BaseSsoRepository;
import com.sysnormal.starters.security.sso.sso_starter.services.records.BaseSsoRecordsService;
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

        return service.get(body).sendHttpResponse();
    }

    @RequestMapping(
            path = { "/{id}", "/get/{id}", "/get/{id}/" },
            method = { RequestMethod.GET, RequestMethod.POST }
    )
    public ResponseEntity<DefaultDataSwap> getById(@PathVariable("id") Long id, @RequestBody(required = false) JsonNode body) {
        return service.getById(id, body).sendHttpResponse();
    }

    @PutMapping("")
    public ResponseEntity<DefaultDataSwap> put(@RequestBody(required = false) JsonNode body) {
        return service.put(body).sendHttpResponse();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DefaultDataSwap> patch(@PathVariable("id") Long id, @RequestBody(required = false) JsonNode body) {
        return service.patch(id, body).sendHttpResponse();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultDataSwap> deleteById(@PathVariable("id") Long id, @RequestBody(required = false) JsonNode body) {
        return service.deleteById(id, body).sendHttpResponse();
    }

    @DeleteMapping("")
    public ResponseEntity<DefaultDataSwap> delete(@RequestBody(required = false) JsonNode body) {
        return service.delete(body).sendHttpResponse();
    }
}

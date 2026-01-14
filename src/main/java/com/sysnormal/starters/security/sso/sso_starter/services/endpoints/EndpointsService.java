package com.sysnormal.starters.security.sso.sso_starter.services.endpoints;

import com.sysnormal.libs.commons.DefaultDataSwap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@Service
public class EndpointsService {

    private static final Logger logger = LoggerFactory.getLogger(EndpointsService.class);

    private Map<String, Object> allEndpoints = null;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    public Map<String, Object> getAllPoints() {
        if (allEndpoints == null) {
            Map<String, Object> endpoints = new HashMap<>();

            Map<RequestMappingInfo, ?> handlerMethods = handlerMapping.getHandlerMethods();
            handlerMethods.forEach((info, handlerMethod) -> {
                info.getPathPatternsCondition()
                        .getPatterns()
                        .forEach(pattern -> {
                                    Map<String, String> endPoint = new HashMap<>();
                                    endPoint.put("path",pattern.getPatternString());
                                    endpoints.put(pattern.getPatternString(), endPoint);
                                }
                        );
            });

            allEndpoints = endpoints;
        }
        return allEndpoints;
    }

    public DefaultDataSwap get(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            result.data = getAllPoints();
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}

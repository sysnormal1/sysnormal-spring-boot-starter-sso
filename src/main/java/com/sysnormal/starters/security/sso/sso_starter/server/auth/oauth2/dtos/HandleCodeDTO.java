package com.sysnormal.starters.security.sso.sso_starter.server.auth.oauth2.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandleCodeDTO {
    private String code;
    private String redirectUri;
}

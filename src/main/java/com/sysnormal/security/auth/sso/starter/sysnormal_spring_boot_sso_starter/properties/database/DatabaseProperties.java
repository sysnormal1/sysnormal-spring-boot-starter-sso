package com.sysnormal.security.auth.sso.starter.properties.database;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * database properties
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.datasource.sso")
@Getter
@Setter
public class DatabaseProperties {

    private boolean enabled = true;
    private String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/my_sso";
    private String username = "root";
    private String password = "masterkey";
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

}
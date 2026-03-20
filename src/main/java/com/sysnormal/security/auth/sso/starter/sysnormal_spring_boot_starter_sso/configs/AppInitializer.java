package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class AppInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    private static AppInitializer INSTANCE;
    private static Environment ENV;



    public AppInitializer() {
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(), "AppInitializer");
        INSTANCE = this;
        logger.debug("END {}.{}",this.getClass().getSimpleName(), "AppInitializer");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(), "initialize");
        ENV = applicationContext.getEnvironment();
        logger.debug("END {}.{}",this.getClass().getSimpleName(), "initialize");
    }

    public static AppInitializer getInstance() {
        return INSTANCE;
    }

    public Environment getENV() {
        return ENV;
    }
}

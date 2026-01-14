package com.sysnormal.starters.security.sso.sso_starter.properties.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Properties;

/**
 * spring properties
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring")
@Getter
@Setter
public class SpringProperties {

    private Flyway flyway = new Flyway();
    private Jpa Jpa = new Jpa();


    @Getter
    @Setter
    public static class Flyway {
        private boolean enabled = false;
        private String[] locations = {"com/sysnormal/starters/security/sso/sso_starter/database/migrations"};
        private boolean baselineOnMigrate = true;


        /**
         * Ensures the library's default migration path is always present
         * If the user sets custom locations, the default one will be appended if missing
         */
        public void setLocations(String[] locations) {
            // Define the default location
            String defaultLocation = "com/sysnormal/starters/security/sso/sso_starter/database/migrations";

            // Se o usuário não passou nada, mantém o padrão
            if (locations == null || locations.length == 0) {
                this.locations = new String[]{defaultLocation};
                return;
            }

            // Verifica se o default já existe (comparando de forma case-insensitive)
            boolean containsDefault = Arrays.stream(locations)
                    .anyMatch(loc -> loc.equalsIgnoreCase(defaultLocation));

            // Se não existe, adiciona
            if (!containsDefault) {
                String[] merged = Arrays.copyOf(locations, locations.length + 1);
                merged[locations.length] = defaultLocation;
                this.locations = merged;
            } else {
                this.locations = locations;
            }
        }
    }



    @Getter
    @Setter
    public static class Jpa {
        private Hibernate hibernate = new Hibernate();
        private Properties properties = new Properties();

        @Getter
        @Setter
        public static class Hibernate {
            private String ddlAuto = "none";
            private String dialect = "org.hibernate.dialect.MySQLDialect";
            private boolean globallyQuotedIdentifiers = true;
        }
    }


}
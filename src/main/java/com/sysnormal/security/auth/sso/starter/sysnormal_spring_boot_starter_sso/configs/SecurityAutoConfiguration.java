package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.configs;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt.JwtProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.security.SecurityProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.auth.filters.CustomAccessDeniedHandler;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.auth.filters.CustomAuthenticationEntryPoint;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.auth.filters.JwtAuthenticationFilter;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt.JwtSsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * SecurityAutoConfiguration
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SecurityProperties.class, JwtProperties.class})
public class SecurityAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAutoConfiguration.class);

    /**
     * the properties
     */
    private final SecurityProperties properties;


    /**
     * default constructor
     *
     * @param properties the properties
     */
    public SecurityAutoConfiguration(SecurityProperties properties) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "SecurityAutoConfiguration");
        this.properties = properties;
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "SecurityAutoConfiguration");
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtSsoService jwtSsoService(JwtProperties jwtProperties) {
        return new JwtSsoService(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtSsoService jwtSsoService, SecurityProperties securityProperties) {
        return new JwtAuthenticationFilter(jwtSsoService,  securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint customAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler customAccessDeniedHandler(ObjectMapper objectMapper) {
        return new CustomAccessDeniedHandler(objectMapper);
    }

    /**
     * cors configure
     *
     * cannot change name of this method, spring internally filter this method by yout name, exactly this name
     *
     * @return the cors configuration
     */
    /*@Bean
    //@ConditionalOnMissingBean(name = "corsConfigurationSource")
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "corsConfigurationSource");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("https://*", "http://*"));
        //config.setAllowedOriginPatterns(List.of("http://localhost:*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "corsConfigurationSource");
        return source;
    }*/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of("*")); // 🔥 qualquer origem
        config.setAllowedMethods(List.of("*"));        // GET, POST, PUT, DELETE, etc
        config.setAllowedHeaders(List.of("*"));        // qualquer header
        config.setAllowCredentials(true);              // se usar cookies/JWT em header
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /*@Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(CorsConfigurationSource source) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "corsFilter");
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "corsFilter");
        return new CorsFilter(source);
    }*/


    /**
     * filter chain
     *
     * @param http the http security
     * @return the security filter chain instance
     * @throws Exception throw on exception
     */
    @Bean
    @ConditionalOnMissingBean(name = "ssoFilterChain")
    public SecurityFilterChain ssoFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "ssoFilterChain");
        SecurityFilterChain result = null;
        try {
            logger.debug("public end points: {}",properties.getPublicEndPoints());
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults()) // enable CORS
                    //.cors(AbstractHttpConfigurer::withDefaults) //disable cors
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler)
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/error").permitAll()
                            .requestMatchers(properties.getPublicEndPoints().toArray(new String[0])).permitAll()
                            .anyRequest().authenticated()
                            //.anyRequest().permitAll()
                    )
                    .addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class);

            result = http.build();
            logger.debug("no errors on  {}.{}", this.getClass().getSimpleName(), "ssoFilterChain");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "ssoFilterChain");
        return result;
    }
}

package it.valeriovaudi.onlyoneportal.budgetservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration(proxyBeanMethods = false)
public class SecurityOAuth2ResourceServerConfig {

    @Value("${granted-role.budget-service}")
    private String grantedRole;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaim("authorities");
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(toList());
        });

        return jwtAuthenticationConverter;
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        return http.csrf().disable()
                .authorizeRequests().mvcMatchers("/actuator/**").permitAll().and()
                .authorizeRequests().anyRequest().hasAnyRole(grantedRole).and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter)
                .and().bearerTokenResolver(bearerTokenResolver())
                .and().build();
    }


    private DefaultBearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        bearerTokenResolver.setAllowFormEncodedBodyParameter(true);
        bearerTokenResolver.setAllowUriQueryParameter(true);
        return bearerTokenResolver;
    }

}

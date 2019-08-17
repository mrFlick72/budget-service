package it.valeriovaudi.familybudget.budgetservice.web.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
/*import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class SecurityOAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and().authorizeRequests().mvcMatchers("/actuator/**").permitAll().and()
                .authorizeRequests().anyRequest().authenticated().and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}*/

@EnableWebSecurity
public class SecurityOAuth2ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        bearerTokenResolver.setAllowFormEncodedBodyParameter(true);
        bearerTokenResolver.setAllowUriQueryParameter(true);
        http.csrf().disable()
                .authorizeRequests().mvcMatchers("/actuator/**").permitAll().and()
                .authorizeRequests().anyRequest().authenticated().and()
                .oauth2ResourceServer().jwt()
        .and().bearerTokenResolver(bearerTokenResolver);
    }

}

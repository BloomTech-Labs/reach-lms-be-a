package com.lambdaschool.oktafoundation.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

// Joel can work below in order to modify which roles have access to which endpoints
// This allows us to further restrict access to an endpoint inside of a controller.
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class OktaAuthSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean()
    {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
            .antMatchers("/",
                "/h2-console/**",
                "/swagger-resources/**",
                "/swagger-resource/**",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**")
            .permitAll()

            // *** NOTE AUTHENTICATED CAN READ USERS!!! PATCHES are handled in UserService
            .antMatchers("/users/**")
            .authenticated()
            // *** Handled at UseremailService Level
            .antMatchers("/useremails/**")
            .authenticated()
            .antMatchers("/modules/**", "/programs/**", "/courses/**", "/students/**")
            .authenticated()
            .antMatchers("/teachers/**")
            .authenticated()
            /*
            .antMatchers("/roles/**")
            .hasAnyRole("ADMIN")
            .antMatchers("/programs/**")
                .hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/courses/**", "/modules/**", "/students/**").permitAll()
            .antMatchers(HttpMethod.POST, "/courses/**", "modules/**", "/teachers/**")
            .hasAnyRole("ADMIN", "TEACHER")
            .antMatchers(HttpMethod.PATCH, "/courses/**", "/modules/**")
            .hasAnyRole("ADMIN", "TEACHER")
            .antMatchers(HttpMethod.PUT, "/courses/**", "/modules/**", "/students/**")
            .hasAnyRole("ADMIN", "TEACHER")
            .antMatchers(HttpMethod.DELETE, "/courses/**", "/modules/**", "/students/**", "/teachers/**")
            .hasAnyRole("ADMIN", "TEACHER")
*/
            // *** Endpoints not specified above are automatically denied
            .anyRequest()
            .denyAll()

            .and()
            .exceptionHandling()
            .and()
            .oauth2ResourceServer()
            .jwt();

        // process CORS annotations
        // http.cors();

        // disable the creation and use of Cross Site Request Forgery Tokens.
        // These tokens require coordination with the front end client that is beyond the scope of this class.
        // See https://www.yawintutor.com/how-to-enable-and-disable-csrf/ for more information
        http
            .csrf()
            .disable();

        // Insert the JwtAuthenticationFilter so that it can grab credentials from the
        // local database before they are checked for authorization (fix by Trevor Buchanan)
        http
            .addFilterBefore(authenticationTokenFilterBean(),
                FilterSecurityInterceptor.class);

        // force a non-empty response body for 401's to make the response more browser friendly
        Okta.configureResourceServer401ResponseBody(http);

        // h2 console
        http.headers()
            .frameOptions()
            .disable();
    }
}
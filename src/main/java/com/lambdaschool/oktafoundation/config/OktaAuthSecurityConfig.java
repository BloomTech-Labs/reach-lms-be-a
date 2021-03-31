package com.lambdaschool.oktafoundation.config;


import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class OktaAuthSecurityConfig
		extends WebSecurityConfigurerAdapter {

	@Bean
	GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults(""); // Removes the ROLE_ prefix
	}

	@Bean
	public JwtAuthenticationFilter authenticationTokenFilterBean() {
		return new JwtAuthenticationFilter();
	}


	@Override
	protected void configure(HttpSecurity http)
	throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// ********************************************************************************
		// WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
		// ********************************************************************************
		// WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
		// ********************************************************************************
		// change to "true" if running locally — ABSOLUTELY DO NOT DEPLOY with "true" here
		if (false) {
			http.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.authorizeRequests()
					.antMatchers("/",
							"/h2-console/**",
							"/v2/api-docs",
							"/webjars/**",
							"/users/**",
							"/useremails/**",
							"/modules/**",
							"/programs/**",
							"/courses/**",
							"/students/**",
							"/teachers/**",
							"/okta/**",
							"/upload/**",
							"/tags/**"
					)
					.permitAll()
					.antMatchers(HttpMethod.GET, "/courses/**", "/modules/**", "/students/**", "/users/**", "/tags/**")
					.permitAll()
					.antMatchers(HttpMethod.POST, "/courses/**", "modules/**", "/teachers/**", "/upload/**", "/tags/**")
					.permitAll()
					.antMatchers(HttpMethod.PATCH, "/courses/**", "/modules/**", "/upload/**", "/tags/**")
					.permitAll()
					.antMatchers(HttpMethod.PUT, "/courses/**", "/modules/**", "/students/**", "/upload/**", "/tags/**")
					.permitAll()
					.antMatchers(HttpMethod.DELETE, "/courses/**", "/modules/**", "/students/**", "/teachers/**", "/tags/**")
					.permitAll()
					.anyRequest()
					.denyAll()
					.and()
					.exceptionHandling()
					.and()
					.oauth2ResourceServer()
					.jwt();
		} else {
			http.authorizeRequests()
					.antMatchers("/", "/h2-console/**", "/webjars/**")
					.permitAll()
					// *** NOTE AUTHENTICATED CAN READ USERS!!! PATCHES are handled in UserService
					.antMatchers("/users/**")
					.authenticated()
					.antMatchers("/modules/**", "/programs/**", "/courses/**", "/students/**", "/tags/**")
					.authenticated()
					.antMatchers("/teachers/**")
					.authenticated()
					.antMatchers(HttpMethod.GET, "/courses/**", "/modules/**", "/students/**", "/users/**", "/tags/**")
					.authenticated()
					.antMatchers(HttpMethod.POST, "/courses/**", "modules/**", "/teachers/**", "/upload/**", "/tags/**")
					.hasAnyRole("ADMIN", "TEACHER")
					.antMatchers(HttpMethod.PATCH, "/courses/**", "/modules/**", "/upload/**", "/tags/**")
					.hasAnyRole("ADMIN", "TEACHER")
					.antMatchers(HttpMethod.PUT, "/courses/**", "/modules/**", "/students/**", "/upload/**", "/tags/**")
					.hasAnyRole("ADMIN", "TEACHER")
					.antMatchers(HttpMethod.DELETE, "/courses/**", "/modules/**", "/students/**", "/teachers/**", "/tags/**")
					.hasAnyRole("ADMIN", "TEACHER")
					// *** Endpoints not specified above are automatically denied
					.anyRequest()
					.denyAll()
					.and()
					.exceptionHandling()
					.and()
					.oauth2ResourceServer()
					.jwt();

		}


		// process CORS annotations
		// http.cors();

		// disable the creation and use of Cross Site Request Forgery Tokens.
		// These tokens require coordination with the front end client that is beyond the scope of this class.
		// See https://www.yawintutor.com/how-to-enable-and-disable-csrf/ for more information
		http.csrf()
				.disable();

		// Insert the JwtAuthenticationFilter so that it can grab credentials from the
		// local database before they are checked for authorization (fix by Trevor Buchanan)
		http.addFilterBefore(authenticationTokenFilterBean(), FilterSecurityInterceptor.class);

		// force a non-empty response body for 401's to make the response more browser friendly
		Okta.configureResourceServer401ResponseBody(http);

		// h2 console
		http.headers()
				.frameOptions()
				.disable();
	}

}
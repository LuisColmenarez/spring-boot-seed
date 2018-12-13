/**
 * 
 */
package com.innova4j.api.app.configuration.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author innova4j-team
 *
 */
@Configuration
@EnableResourceServer
public class RestApiResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	/**
	 * Put here your public access endpoints...
	 */
	private static final String[] PUBLIC_ACCESS = new String[] { "/reset-password**", "/update-password" };
	/**
	 * Put here your client access endpoints...
	 */
	private static final String[] CLIENT_ACCESS = new String[] {};
	/**
	 * Put here your full authentication access endpoints...
	 */
	private static final String[] FULL_AUTHENTICATION_ACCESS = new String[] {};

	@Value("${app.name}")
	private String resourceId;

	/**
	 * 
	 * @return The CORS configuration source.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(Boolean.TRUE);
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 * 
	 * @return The CORS filter.
	 */
	@Bean
	public CorsFilter corsFilter() {
		return new CorsFilter(corsConfigurationSource());
	}

	/**
	 * 
	 * @return The custom CORS filter
	 */
	@Bean
	public CustomCorsFilter customCorsFilter() {
		return new CustomCorsFilter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.
	 * ResourceServerConfigurerAdapter#configure(org.springframework.security.oauth2
	 * .config.annotation.web.configurers.ResourceServerSecurityConfigurer)
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(resourceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.
	 * ResourceServerConfigurerAdapter#configure(org.springframework.security.config
	 * .annotation.web.builders.HttpSecurity)
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				// CORS...
				.csrf().disable()
				// Filters...
				.addFilterBefore(customCorsFilter(), SessionManagementFilter.class)
				// OAuth OPTIONS requests...
				.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
				// Public access...
				.antMatchers(PUBLIC_ACCESS).permitAll()
				// Client credentials access...
				.antMatchers(CLIENT_ACCESS).access("#oauth2.isClient()")
				// Setup full authentication access...
				.antMatchers("/oauth/user-info", "/oauth/update-password/**", "/clients**", "/users**").authenticated()
				.antMatchers(FULL_AUTHENTICATION_ACCESS).authenticated();
	}

}

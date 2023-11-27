package com.school.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class MyConfig implements WebMvcConfigurer {

	@Autowired
	private InvalidUserAuthEntryPoint invalidUserAuthEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private SecurityFilter securityFilter;

	// @Bean
	// @Override
	// public void addCorsMappings(CorsRegistry registry) {
	// registry.addMapping("/**")
	// .allowedOriginPatterns("http://localhost:4200")
	// .allowedMethods("GET", "POST", "PUT", "DELETE")
	// .allowedHeaders("*")
	// .allowCredentials(true);
	// }

	@Bean
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.disable())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/addUser", "/admin/login").permitAll()
						.anyRequest().authenticated())
				.exceptionHandling(handling -> handling
						.authenticationEntryPoint(invalidUserAuthEntryPoint))
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// register filter for 2nd request onwards..
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

}
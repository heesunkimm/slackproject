package com.example.slackproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(request -> request
					.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
					.requestMatchers("/resources/**").permitAll()
					.requestMatchers("/authoerror", "/error", "/", "/login", "/join", "/idCheck").permitAll()
					.requestMatchers("/userManagement", "/userStatusUpdate").hasRole("ADMIN")
					.anyRequest().authenticated()
			)
			.exceptionHandling(exception -> exception
					.authenticationEntryPoint(new CustomAuthenticationEntryPoint())

			)
			.formLogin(form -> form
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.usernameParameter("userId")
					.passwordParameter("userPw")
					// .defaultSuccessUrl("/index", true)
					.successHandler(new CustomAuthenticationSuccessHandler())
					.failureHandler(new CustomAuthenticationFailureHandler())
					.permitAll()
			)
			.logout(Customizer.withDefaults());
	
		return http.build();
	}
}

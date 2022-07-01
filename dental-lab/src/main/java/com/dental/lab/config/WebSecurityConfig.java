package com.dental.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dental.lab.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService())
			.passwordEncoder(passwordEncoder());
	}
	
	@Override                                                                                                                                               
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}
	
	@Order(1)
	@Configuration
	public static class RestJwtSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/api/v1/**")
				.authorizeRequests()
					.anyRequest().permitAll();
		}
	}
	
	@Order(2)
	@Configuration
	public static class webAppSecurityConfig extends WebSecurityConfigurerAdapter {
		

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/**")
				.authorizeRequests()
					.antMatchers("/home", "/").permitAll()
					.antMatchers("/orders/create").hasAnyRole("ADMIN", "CLIENT", "DENTIST")
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.loginPage("/login")
					.permitAll()
					.failureUrl("/login?login_error=true")
					.defaultSuccessUrl("/home", true)
					.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.deleteCookies("JSESSIONID", "JWT");
		}
		
	}
	
	

}

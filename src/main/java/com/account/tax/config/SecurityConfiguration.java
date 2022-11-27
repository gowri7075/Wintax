package com.account.tax.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.account.tax.service.TaxService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TaxService userService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    
   
   
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
			.antMatchers("/admin",
					"/referralRequests",
					"/contactRequests",
					"/displayDocuments",
					"/usersInfo").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/home",
					"/dashboard").access("hasRole('ROLE_USER')")
            .antMatchers(
                "/register**",
                "/forgotpassword**",
                "/forgotPassword**",
                "/reset_password**",
                "/",
                "/h2",
                "/h2/login**",
                "/h2/test**",
                "/h2-console/**",
                "/contact",
                "/refer",
                "/contactAdminFromHome",
                "/contactAdmin",
                "/js/**",
                "/css/**",
                "/scss/**",
                "/fonts/**",
                "/images/**",
                "/vendor/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("email")
            .successHandler(authenticationSuccessHandler)
            .permitAll()
            .and()
            .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout")
            .permitAll().and()
  		  .exceptionHandling().accessDeniedPage("/accessdenied");
        
        http.requiresChannel()
        .antMatchers("/login** ").requiresSecure();
        
        http.csrf().disable();
        http.headers().frameOptions().disable();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
}
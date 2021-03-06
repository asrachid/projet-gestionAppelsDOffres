package com.example.sec;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select email, password, active from utilisateur where email=?")
		.authoritiesByUsernameQuery("select email, role from utilisateur where email=?")
				/* .passwordEncoder(new BCryptPasswordEncoder()) */
		.rolePrefix("ROLE_")
		.passwordEncoder(NoOpPasswordEncoder.getInstance())
		;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/appelOffres").permitAll()
			.antMatchers("/viewAnnonce/*").permitAll()
			.antMatchers("/docsOff").permitAll()
			.antMatchers("/downloadDoc/*").permitAll()
			.antMatchers("/downloadFile/*").permitAll()
			.antMatchers("/inscription").permitAll()
			.antMatchers("/downloadFormInscription").permitAll()
			.antMatchers("/portailAP").hasRole("AP")
			.antMatchers("/soumissionner").hasRole("SM")
			.antMatchers("/portailAdmin").hasRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.permitAll()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/portailAP") 
			.and()
			.logout()
				.permitAll()
				.logoutSuccessUrl("/appelOffres")
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			;
	}
	
	
}

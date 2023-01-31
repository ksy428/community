package hello.community.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import hello.community.global.security.CustomLoginFailureHandler;
import hello.community.global.security.CustomLoginSuccessHandler;
import hello.community.global.security.UserDetailsServiceImpl;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig{
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Bean
	public PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			.csrf().disable()
				.authorizeHttpRequests()
				.antMatchers("/").permitAll()
				.antMatchers("admin/**").hasAnyRole("ADMIN")
			.and()
				.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login")
					.usernameParameter("loginId")
					.passwordParameter("password")
					.permitAll()
					.successHandler(customLoginSuccessHandler())
					.failureHandler(customLoginFailureHandler())
				//.successForwardUrl("/")
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.rememberMe()
				.key("seyoung")
				.rememberMeParameter("remember-me")
				.tokenValiditySeconds(86400 * 30)
				.userDetailsService(userDetailsServiceImpl)
				.authenticationSuccessHandler(customLoginSuccessHandler());
		
		return http.build();
	}
	
	@Bean
	public CustomLoginSuccessHandler customLoginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
	
	@Bean
	public CustomLoginFailureHandler customLoginFailureHandler() {
		return new CustomLoginFailureHandler();
	}
	
}

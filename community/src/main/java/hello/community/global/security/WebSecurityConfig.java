package hello.community.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig{

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
					.successHandler(new CustomLoginSuccessHandler())
					.failureHandler(new CustomLoginFailureHandler())
				//.successForwardUrl("/")
			.and()
				.logout()
				.logoutSuccessUrl("/");
		
		return http.build();
	}
}

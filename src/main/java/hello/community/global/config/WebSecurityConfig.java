package hello.community.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import hello.community.global.security.CustomLoginFailureHandler;
import hello.community.global.security.CustomLoginSuccessHandler;
import hello.community.global.security.UserDetailsServiceImpl;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
public class WebSecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Bean
	public PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	}

	// AuthenticationManager 커스텀
	// 시큐리티는 인증이 완료되면 Credentials(인증서)를 지워버리는데 회원정보 변경 후 세션 갱신을 위해서
	// eraseCredentials(false)을 설정해서 지우지 않도록 함
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.eraseCredentials(false)
				.authenticationProvider(rememberMeAuthenticationProvider())
				.userDetailsService(userDetailsServiceImpl)
				.passwordEncoder(passwordEncoder());

		return authenticationManagerBuilder.build();
	}

	@Bean
	public RememberMeServices tokenBasedRememberMeServices() {
		TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices("seyoung", userDetailsServiceImpl);
		tokenBasedRememberMeServices.setAlwaysRemember(true);
		tokenBasedRememberMeServices.setParameter("remember-me");
		tokenBasedRememberMeServices.setTokenValiditySeconds(86400 * 30);

		return tokenBasedRememberMeServices;
	}

	@Bean
	public RememberMeAuthenticationFilter rememberMeAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices){

		return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
	}
	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
		return new RememberMeAuthenticationProvider("seyoung");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{

		//커스텀한 AuthenticationManager 등록
		http.authenticationManager(authenticationManager)
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
				.exceptionHandling().accessDeniedPage("/login")
			.and()
				.rememberMe().key("seyoung")
				.authenticationSuccessHandler(customLoginSuccessHandler())
			.and()
				.addFilter(rememberMeAuthenticationFilter(authenticationManager, tokenBasedRememberMeServices()));

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

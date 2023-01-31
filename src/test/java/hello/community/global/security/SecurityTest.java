package hello.community.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.member.Member;
import hello.community.global.util.DateUtil;
import hello.community.repository.member.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class SecurityTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	EntityManager em;
	@Autowired
	PasswordEncoder passwordEncoder;

	private static String username = "test";
	private static String password = "1234";

	/*@BeforeEach
	public void init() {
	
		memberRepository.save(Member.builder()			
				.loginId("test")
				.password(passwordEncoder.encode("1234"))
				.nickname("testZZ")
				.email("test@naver.com")
				.build());
	}
	
		@Test
		void 로그인테스트() throws Exception{
			
			mockMvc.perform(formLogin().userParameter("loginId").user(username).password(password))
				.andExpect(authenticated());
		}*/
}

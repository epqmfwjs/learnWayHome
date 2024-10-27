package com.learnway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.learnway.consult.service.ConsultantService;
import com.learnway.member.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;


// 시큐리티를 설정하는 Security Config 클래스
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Spring Security에서 사용자 인증을 위한 사용자 세부 정보를 제공하는 커스텀 서비스
    private final CustomUserDetailsService userDetailsService;
    private final ConsultantService consultantService;

    @Bean // Bcrypt 암호화 메서드 : 사용자 인증 시 필요한 비밀번호를 암호화하여 저장
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Static(정적 리소스 ) 정상 로드를 위해 하기 요청은 시큐리티 필터체인 Ignoring 적용
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/img/**", "/js/**");
    }

    @Bean
    @Order(1) // 우선 순위 첫번째로 보안 설정 적용됨
    public SecurityFilterChain counselorSecurityFilterChain(HttpSecurity http) throws Exception {
        logger.info("counselor security filter chain");
        // 상담사 보안 설정 test
        http
                .securityMatcher("/consult/**")
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/index-consult","/loginChange/consult","/login-consult").permitAll()
                                .requestMatchers("/consult/**").hasRole("COUNSELOR")
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/consult/loginChange/consult")      //로그인 페이지 경로
                                .loginProcessingUrl("/consult/login-consult")   //
                                .defaultSuccessUrl("/consult/consultant", true)
                                .failureUrl("/consult/loginChange/consult?error=true")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/counselor/logout")
                                .logoutSuccessUrl("/index-consult")
                                .permitAll()
                )
                .userDetailsService(consultantService)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionFixation().migrateSession()
                                .maximumSessions(1)

                )
                .authorizeRequests()
                .anyRequest().denyAll();

        return http.build();
    }

    @Bean // 인증 인가를 검증하는 필터 체인
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 일반, 관리자 인증-인가 필터체인
        logger.info("main security filter chain");
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/member/join","/**","/api/**").permitAll() // 비회원 포함 모든 권한 접근 가능
                                .requestMatchers("/admin/**").hasRole("ADMIN")      // Admin 권한만 접근 가능
                                .requestMatchers("/counselor/**").denyAll()         // 일반 사용자 counselor 접근 제한
                                .requestMatchers("/loginOk","/api/**").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated()                         // 그 외 조건은 인증 필요
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/")                 // 로그인 페이지
                                .loginProcessingUrl("/login")   // 로그인 요청 경로
                                .defaultSuccessUrl("/loginOk", true)   // 성공 시 리디렉션 경로
                                .failureUrl("/?error=true")                              // 실패 시 경로
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll()
                )
                .userDetailsService(userDetailsService)
                .sessionManagement(sessionManagement ->
                        sessionManagement                           // 세션 설정
                                .sessionFixation().migrateSession() // 세션 고정 보호 설정
                                // migrateSession(): 사용자가 로그인할 때 새로운 세션을 생성, 기존 세션의 속성 새 세션으로 복사
                                // 세션 고정 공격 : 악의적으로 다른 사용자의 세션 ID를 탈취하여 해당 세션을 사용하는 공격
                                .maximumSessions(1)                 // 최대 동시 세션 수
                ).csrf().disable();

        return http.build();
    }
    @Bean
    @Order(3)
    public SecurityFilterChain httpsSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(requiresChannel ->
                        requiresChannel
                                .requestMatchers("/signaling/**").requiresSecure()  // HTTPS가 필요한 URL 패턴
                                .anyRequest().requiresInsecure()  // 나머지 URL은 HTTP로
                );
        return http.build();
    }

}

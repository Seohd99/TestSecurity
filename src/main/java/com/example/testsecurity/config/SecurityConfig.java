package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 암호화 메소드
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ // 예외처리 필수
//        경로별 권한 지정 위에서 부터 아래로 먼저 적용
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/loginProc","/join","/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/login")       // 권한 부족시 에러 페이지가 아닌  login 페이지로
                        .loginProcessingUrl("/loginProc")           // 특정 경로로 보냄
                        .permitAll()                                // 접근 가능한 권한 permitAll : 모두 접근 가능
                );

//        // 사이트 위변조 기능 Default로 enable인데 그 동작을 끄는것
//        http
//                .csrf((auth) -> auth.disable());

        // 로그아웃
        http
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));

        // 다중 로그인 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)                     // 최대 허용 중복 접속
                        .maxSessionsPreventsLogin(true));       // 최대를 넘어가면 어떻게 처리할지 true : 초과접속 막기, false : 기존 접속 로그아웃

        // 로그인 세션 고정 공격 보호
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());  // 로그인 시 동일한 세션에 대한 id 변경 none : 로그인 시 세션 정보 변경 안함 , newSession : 로그인 시 세션 새로 생성
        return http.build();
    }
}
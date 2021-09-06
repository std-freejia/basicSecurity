package io.spring.basicSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity // 웹 보안 활성화 시킴
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 인가
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        // 인증 - form 기반
        http
                .formLogin()
                // .loginPage("/loginPage") // 로그인 폼 페이지 (인증 받지 않은 누구나 접근 가능한 페이지)
                .defaultSuccessUrl("/") // 인증에 성공했을 때 이동할 url
                .failureUrl("/login")
                .usernameParameter("userId") // input 필드명 지정 가능
                .passwordParameter("passwd") // input 필드명 지정 가능
                .loginProcessingUrl("/login_proc") // form 태그의 action url 속성 지정
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override // 로그인 성공시,
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication = " + authentication.getName()); // 인증에 성공한 사용자이름
                        response.sendRedirect("/");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override // 인증 실패시,
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        // 다시 로그인할 수 있는 로그인 페이지로 보냄
                        System.out.println("e.getMessage() = " + e.getMessage());
                        response.sendRedirect("/login");
                    }
                })
                .permitAll();
    }

}

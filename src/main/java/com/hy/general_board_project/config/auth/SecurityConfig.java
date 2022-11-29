package com.hy.general_board_project.config.auth;

import com.hy.general_board_project.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/user/login", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/board/write", "/board/detail").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and() // 로그인 설정
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
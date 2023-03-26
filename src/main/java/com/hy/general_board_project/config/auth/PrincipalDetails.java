package com.hy.general_board_project.config.auth;

import com.hy.general_board_project.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    // 일반 로그인 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // User 권한을 리턴하는 메서드
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRoleKey();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() { // User 비밀번호 리턴
        return user.getPassword();
    }

    @Override
    public String getUsername() { // User PK 또는 고유한 값을 리턴
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { // User 계정 만료 여부 리턴
        return true; // true : 만료 안됨
    }

    @Override
    public boolean isAccountNonLocked() { // User 계정 잠김 여부 리턴
        return true; // true : 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() { // User 비밀번호 만료 여부 리턴
        return true; // true : 만료 안됨
    }

    @Override
    public boolean isEnabled() { // User 계정 활성화 여부 리턴
        return true; // true : 활성화 됨
    }

    // === OAuth2User 메서드 === //
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String picture;
    private Role role;
    private String provider;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public UserDto(Long id, String username, String password, String nickname, String email, String picture, Role role, String provider, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .picture(picture)
                .role(role)
                .provider(provider)
                .build();
    }
}
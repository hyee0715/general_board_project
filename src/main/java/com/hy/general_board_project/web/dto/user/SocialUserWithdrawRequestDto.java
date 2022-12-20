package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SocialUserWithdrawRequestDto implements UserDto {

    @NotBlank
    private String nickname;

    @NotBlank
    private String email;

    private String provider;

    @NotBlank
    private String requestEmail;

    @Builder
    public SocialUserWithdrawRequestDto(String nickname, String email, String provider, String requestEmail) {
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.requestEmail = requestEmail;
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .provider(provider)
                .build();
    }
}

package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SocialUserWithdrawRequestDto implements UserDto {

    private Long id;

    @NotBlank
    private String email;

    private String provider;

    @NotBlank
    private String requestEmail;

    @Builder
    public SocialUserWithdrawRequestDto(Long id, String email, String provider, String requestEmail) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.requestEmail = requestEmail;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .provider(provider)
                .build();
    }
}

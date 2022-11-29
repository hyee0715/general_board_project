package com.hy.general_board_project.config.auth.dto;

import com.hy.general_board_project.domain.user.SocialUser;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(SocialUser socialUser) {
        this.name = socialUser.getName();
        this.email = socialUser.getEmail();
        this.picture = socialUser.getPicture();
    }
}
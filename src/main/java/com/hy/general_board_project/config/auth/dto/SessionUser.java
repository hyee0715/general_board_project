package com.hy.general_board_project.config.auth.dto;

import com.hy.general_board_project.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String username;
    private String email;
    private String picture;
    private String provider;

    public SessionUser(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.provider = user.getProvider();
    }
}
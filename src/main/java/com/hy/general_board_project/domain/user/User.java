package com.hy.general_board_project.domain.user;

import com.hy.general_board_project.domain.Time;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String realName;

    @Column(nullable = false)
    private String username;

    private String password;

    @Column
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String provider;

    private String certified;

    @Builder
    public User(Long id, String realName, String username, String password, String nickname, String email, String picture, Role role, String provider, String certified) {
        this.id = id;
        this.realName = realName;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.certified = certified;
    }

    public User update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }

    public User updateNickname(String nickname) {
        this.nickname = nickname;

        return this;
    }

    public User updatePassword(String password) {
        this.password = password;

        return this;
    }

    public User updateCertified() {
        this.certified = "Y";

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
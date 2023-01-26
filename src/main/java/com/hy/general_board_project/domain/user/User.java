package com.hy.general_board_project.domain.user;

import com.hy.general_board_project.domain.Time;
import com.hy.general_board_project.domain.profileImage.ProfileImage;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String provider;

    private String certified;

    @OneToOne
    @JoinColumn(name = "PROFILE_IMAGE_ID")
    private ProfileImage profileImage;

    @Builder
    public User(Long id, String realName, String username, String password, String nickname, String email, Role role, String provider, String certified, ProfileImage profileImage) {
        this.id = id;
        this.realName = realName;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.certified = certified;
        this.profileImage = profileImage;
    }

    public User updateForSocial(String username, String email) {
        this.username = username;
        this.email = email;

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

    public User updateProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;

        return this;
    }
}
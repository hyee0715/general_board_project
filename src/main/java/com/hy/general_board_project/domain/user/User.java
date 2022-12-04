package com.hy.general_board_project.domain.user;

import com.hy.general_board_project.domain.Time;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class User extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Builder
    public User(String username, String nickname, String email, String picture, Role role, String provider) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
    }

    public User update(String name, String picture) {
        this.username = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
package com.hy.general_board_project.domain.profileImage;

import com.hy.general_board_project.domain.Time;
import com.hy.general_board_project.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProfileImage extends Time {

    @Id
    @Column(name = "PROFILE_IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadName;
    private String storeName;

    @OneToOne(mappedBy = "profileImage")
    private User user;

    @Builder
    public ProfileImage(Long id, String uploadName, String storeName, User user) {
        this.id = id;
        this.uploadName = uploadName;
        this.storeName = storeName;
        this.user = user;
    }
}
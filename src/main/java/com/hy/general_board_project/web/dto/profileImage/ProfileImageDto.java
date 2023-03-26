package com.hy.general_board_project.web.dto.profileImage;

import com.hy.general_board_project.domain.profileImage.ProfileImage;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfileImageDto {

    private String uploadName;
    private String storeName;

    @Builder
    public ProfileImageDto(String uploadName, String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }

    public ProfileImage toEntity() {
        return ProfileImage.builder()
                .uploadName(uploadName)
                .storeName(storeName)
                .build();
    }
}

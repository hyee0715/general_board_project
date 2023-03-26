package com.hy.general_board_project.domain.profileImage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findById(Long id);
}
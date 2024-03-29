package com.hy.general_board_project.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndProvider(String email, String provider);

    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmailAndProvider(String email, String provider);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmailAndCertified(String email, String certified);
}
package com.hy.general_board_project.config.auth;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntityWrapper = userRepository.findByUsername(username);

        User userEntity = userEntityWrapper.get();

        return new PrincipalDetails(userEntity);
    }
}
package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.repository.UserRepository;
import com.smarteshop_backend.security.CustomUserDetails;
import com.smarteshop_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    /**
     * Load user by username (return UserDetails object)
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByAccountUsername(username);

        return user.map(CustomUserDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Cannot find user by username: " + username)
                );
    }

    /**
     * Get user by username (return User object)
     *
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public User getByUsername(String username) throws Exception {
        User user = userRepository.findByAccountUsername(username)
                .orElseThrow(
                        () -> new Exception("Can not find user by username = " + username)
                );
        return user;
    }

    /**
     * Save user
     *
     * @param user
     * @return
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}

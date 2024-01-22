package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.dto.response.FormGetUser;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByAccountUsername(username);

        return user.map(CustomUserDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Cannot find user by username: " + username)
                );
    }

    @Override
    public FormGetUser getByUsername(String username) throws Exception {
        User user = userRepository.findByAccountUsername(username)
                .orElseThrow(
                        () -> new Exception("Can not find user by username = " + username)
                );
        return new FormGetUser(
                user.getId(),
                user.getFullname(),
                user.getAccount().getUsername(),
                user.getAccount().getEmail(),
                user.getAccount().getEnabled()
        );
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
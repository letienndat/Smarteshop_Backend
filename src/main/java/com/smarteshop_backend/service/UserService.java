package com.smarteshop_backend.service;

import com.smarteshop_backend.dto.response.FormGetUser;
import com.smarteshop_backend.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    FormGetUser getByUsername(String username) throws Exception;
    User save(User user);
}

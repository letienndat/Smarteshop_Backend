package com.smarteshop_backend.security;

import com.smarteshop_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Autowired
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of("USER").map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return user.getAccount().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


package com.smarteshop_backend.util;

import org.springframework.stereotype.Service;

@Service
public interface PasswordGenerator {
    String generatePassword(int length);
}
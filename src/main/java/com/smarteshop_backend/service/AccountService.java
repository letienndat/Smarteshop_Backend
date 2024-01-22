package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    Account registerAccount(Account account);
    void sendVerificationEmail(Account account);
    boolean verifyAccount(String username, String verificationCode);
    boolean existsAccountByUsername(String username);
    Account findAccountByUsername(String username);
    void resendVerificationEmail(Account account);
}

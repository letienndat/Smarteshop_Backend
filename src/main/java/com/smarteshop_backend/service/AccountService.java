package com.smarteshop_backend.service;

import com.smarteshop_backend.dto.response.FormGetAccount;
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
    Account forgotPassword(Account account);
    void sendForgotPasswordEmail(Account account);
    Account newPassword(Account account);
    void sendNewPasswordEmail(Account account, String newPassword);
}

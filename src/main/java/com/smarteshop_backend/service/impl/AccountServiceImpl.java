package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Account;
import com.smarteshop_backend.repository.AccountRepository;
import com.smarteshop_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account registerAccount(Account account) {
        account.setVerificationCode(generateVerificationCode());
        account.setExpiredTime(new Date());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        sendVerificationEmail(account);

        return account;
    }

    @Override
    public void sendVerificationEmail(Account account) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("Xác thực tài khoản");
        message.setText("Mã xác thực của bạn là: " + account.getVerificationCode());

        javaMailSender.send(message);
    }

    @Override
    public void resendVerificationEmail(Account account) {
        account.setVerificationCode(generateVerificationCode());
        account.setExpiredTime(new Date());
        accountRepository.save(account);

        sendVerificationEmail(account);
    }

    @Override
    public boolean verifyAccount(String username, String verificationCode) {
        Account account = accountRepository.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("Cannot find user by username: " + username)
        );

        Date timeObject = account.getExpiredTime();
        Date timeNow = new Date();

        long differenceInMillis = Math.abs(timeObject.getTime() - timeNow.getTime());
        long differenceInSeconds = differenceInMillis / 1000;

        if (differenceInSeconds > 60) {
            return false;
        }

        if (account.getVerificationCode().equals(verificationCode)) {
            account.setEnabled(true);
            accountRepository.save(account);

            return true;
        }

        return false;
    }

    private String generateVerificationCode() {
        int code = Math.abs(UUID.randomUUID().hashCode() % 900000) + 100000;

        return String.valueOf(code);
    }

    @Override
    public boolean existsAccountByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}

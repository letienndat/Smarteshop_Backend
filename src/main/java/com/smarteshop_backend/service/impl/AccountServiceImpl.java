package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Account;
import com.smarteshop_backend.repository.AccountRepository;
import com.smarteshop_backend.service.AccountService;
import com.smarteshop_backend.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    /**
     * Register account
     *
     * @param account
     * @return
     */
    @Override
    public Account registerAccount(Account account) {
        account.setVerificationCode(generateVerificationCode());
        account.setExpiredTime(new Date());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        sendVerificationEmail(account);

        return account;
    }

    /**
     * Send code verification to mail
     *
     * @param account
     */
    @Override
    public void sendVerificationEmail(Account account) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("Xác thực tài khoản");
        message.setText("Mã xác thực của bạn là: " + account.getVerificationCode());

        javaMailSender.send(message);
    }

    /**
     * Resend code verification to mail
     *
     * @param account
     */
    @Override
    public void resendVerificationEmail(Account account) {
        account.setVerificationCode(generateVerificationCode());
        account.setExpiredTime(new Date());
        accountRepository.save(account);

        sendVerificationEmail(account);
    }

    /**
     * Forgot password
     *
     * @param account
     * @return
     */
    @Override
    public Account forgotPassword(Account account) {
        account.setVerificationCode(generateVerificationCode());
        account.setExpiredTime(new Date());
        accountRepository.save(account);

        sendForgotPasswordEmail(account);

        return account;
    }

    /**
     * Send code forgot password to mail
     *
     * @param account
     */
    @Override
    public void sendForgotPasswordEmail(Account account) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("Yêu cầu lấy lại mật khẩu");
        message.setText("Mã xác thực của bạn là: " + account.getVerificationCode());

        javaMailSender.send(message);
    }

    /**
     * Create and save new password
     *
     * @param account
     * @return
     */
    @Override
    public Account newPassword(Account account) {
        Random random = new Random();
        int length = random.nextInt(9) + 8;
        String newPassword = passwordGenerator.generatePassword(length);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        sendNewPasswordEmail(account, newPassword);

        return account;
    }

    /**
     * Send new password to mail
     *
     * @param account
     * @param newPassword
     */
    @Override
    public void sendNewPasswordEmail(Account account, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("Mật khẩu đã được thay đổi");
        message.setText("Mật khẩu của bạn đã được thay đổi, hãy sử dụng mật khẩu mới này để đăng nhập: " + newPassword);

        javaMailSender.send(message);
    }

    /**
     * Verify account
     *
     * @param username
     * @param verificationCode
     * @return
     */
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

    /**
     * Generate verification code
     *
     * @return
     */
    private String generateVerificationCode() {
        int code = Math.abs(UUID.randomUUID().hashCode() % 900000) + 100000;

        return String.valueOf(code);
    }

    /**
     * Check exists account by username
     *
     * @param username
     * @return
     */
    @Override
    public boolean existsAccountByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    /**
     * Find account by username
     *
     * @param username
     * @return
     */
    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}

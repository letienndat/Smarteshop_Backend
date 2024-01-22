package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.*;
import com.smarteshop_backend.dto.response.FormGetUser;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.Account;
import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.service.impl.AccountServiceImpl;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signin")
    public ResponseEntity<MessageResponse> signin(@Valid @RequestBody FormSignin formAccount) throws Exception {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(formAccount.getUsername(), formAccount.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            FormGetUser formGetUser = userService.getByUsername(formAccount.getUsername());
            if (!formGetUser.getAccountEnabled()) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.NOT_ACTIVE, formGetUser)
                );
            }
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, formGetUser)
            );
        }

        return ResponseEntity.status(401)
                .body(
                        new MessageResponse(
                                TypeMessage.FALD,
                                "validate_fail"
                        )
                );
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody FormSignUp formSignUp) throws Exception {
        if (accountService.existsAccountByUsername(formSignUp.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exist_account")
            );
        }

        Account registerAccount = new Account(
                formSignUp.getUsername(),
                formSignUp.getEmail(),
                formSignUp.getPassword(),
                null,
                false,
                null
        );

        User createUser = new User(
                null,
                formSignUp.getFullname(),
                null,
                "",
                null,
                null,
                null
        );

        Account account = accountService.registerAccount(registerAccount);
        createUser.setAccount(account);

        userService.save(createUser);

        FormGetUser formGetUser = modelMapper.map(createUser, FormGetUser.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, formGetUser)
        );
    }

    @PostMapping("/active_account")
    public ResponseEntity<MessageResponse> activeAccount(@Valid @RequestBody FormActiveAccount formActiveAccount) throws Exception {
        if (!accountService.existsAccountByUsername(formActiveAccount.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_user")
            );
        }

        Account accountFound = accountService.findAccountByUsername(formActiveAccount.getUsername());

        if (accountFound.getEnabled()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "account_already_confirmed")
            );
        }

        boolean checkCode = accountService.verifyAccount(formActiveAccount.getUsername(), formActiveAccount.getCode());

        if (!checkCode) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_validate_code")
            );
        }

        FormGetUser formGetUser = userService.getByUsername(formActiveAccount.getUsername());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, formGetUser)
        );
    }

    @PostMapping("/send_code")
    public ResponseEntity<MessageResponse> sendCodeVerifyAccount(@Valid @RequestBody FormSendCodeVerifyAccount formSendCodeVerifyAccount) throws Exception {
        if (!accountService.existsAccountByUsername(formSendCodeVerifyAccount.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_user")
            );
        }

        Account accountFound = accountService.findAccountByUsername(formSendCodeVerifyAccount.getUsername());

        if (accountFound.getEnabled()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "account_already_confirmed")
            );
        }

        accountService.resendVerificationEmail(accountFound);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "send_code_to_mail_ok")
        );
    }
}

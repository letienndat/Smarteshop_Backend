package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormFindAccount;
import com.smarteshop_backend.dto.request.FormSendCodeVerifyAccount;
import com.smarteshop_backend.dto.request.FormValidateCodeForgotPassword;
import com.smarteshop_backend.dto.response.FormGetAccount;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.Account;
import com.smarteshop_backend.service.AccountService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/find-account")
    public ResponseEntity<MessageResponse> findAccount(@Valid @RequestBody FormFindAccount formFindAccount) {
        Account account = accountService.findAccountByUsername(formFindAccount.getUsername());
        FormGetAccount formGetAccount = modelMapper.map(account, FormGetAccount.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetAccount)
        );
    }

    @GetMapping("/forgot-password-get-account")
    public ResponseEntity<MessageResponse> getAccountAndSendCode(@Valid @RequestBody FormFindAccount formFindAccount) {
        if (!accountService.existsAccountByUsername(formFindAccount.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_account", null)
            );
        }

        Account account = accountService.findAccountByUsername(formFindAccount.getUsername());
        accountService.forgotPassword(account);

        FormGetAccount formGetAccount = modelMapper.map(account, FormGetAccount.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetAccount)
        );
    }

    @PostMapping("/send-code")
    public ResponseEntity<MessageResponse> sendCodeVerifyAccount(@Valid @RequestBody FormSendCodeVerifyAccount formSendCodeVerifyAccount) throws Exception {
        if (!accountService.existsAccountByUsername(formSendCodeVerifyAccount.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_user", null)
            );
        }

        Account accountFound = accountService.findAccountByUsername(formSendCodeVerifyAccount.getUsername());

        accountService.resendVerificationEmail(accountFound);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "send_code_to_mail_ok", null)
        );
    }

    @PostMapping("/validate-code-forgot-password")
    public ResponseEntity<MessageResponse> validateCodeForgotPassword(@Valid @RequestBody FormValidateCodeForgotPassword formValidateCode) {
        if (!accountService.existsAccountByUsername(formValidateCode.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_user", null)
            );
        }

        boolean validateCode = accountService.verifyAccount(formValidateCode.getUsername(), formValidateCode.getCode());

        if (validateCode) {
            Account account = accountService.findAccountByUsername(formValidateCode.getUsername());

            accountService.newPassword(account);

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "validate_code_ok_and_sended_new_password_to_mail", null)
            );
        }

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.FALD, "validate_code_fail", null)
        );
    }
}

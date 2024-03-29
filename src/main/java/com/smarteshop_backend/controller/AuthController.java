package com.smarteshop_backend.controller;

import com.smarteshop_backend.const_app.ConstApp;
import com.smarteshop_backend.dto.request.*;
import com.smarteshop_backend.dto.response.FormGetUser;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.RoleService;
import com.smarteshop_backend.service.ShopCartService;
import com.smarteshop_backend.service.impl.AccountServiceImpl;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Signin account
     *
     * @param formAccount
     * @return
     * @throws Exception
     */
    @PostMapping("/signin")
    public ResponseEntity<MessageResponse> signin(@Valid @RequestBody FormSignin formAccount) throws Exception {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(formAccount.getUsername(), formAccount.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            User user = userService.getByUsername(formAccount.getUsername());
            FormGetUser formGetUser = modelMapper.map(user, FormGetUser.class);
            if (!formGetUser.getAccountEnabled()) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.NOT_ACTIVE, "account_is_not_active", formGetUser)
                );
            }
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "login_complete", formGetUser)
            );
        }

        return ResponseEntity.status(401)
                .body(
                        new MessageResponse(
                                TypeMessage.FALD,
                                "validate_fail",
                                null
                        )
                );
    }

    /**
     * Signup account
     *
     * @param formSignUp
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody FormSignUp formSignUp) {
        if (accountService.existsAccountByUsername(formSignUp.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exist_account", null)
            );
        }

        List<Role> roles = new ArrayList<>();
        try {
            Role role = roleService.findByRoleName(RoleName.USER);
            roles.add(role);
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_role", null)
            );
        }

        Account registerAccount = new Account(
                formSignUp.getUsername(),
                formSignUp.getEmail(),
                formSignUp.getPassword(),
                roles,
                null,
                false,
                null
        );

        String pathAvatar = "/" + ConstApp.PREFIX_PATH_AVATAR + "/" + ConstApp.PREFIX_PATH_DEFAULT;
        User createUser = new User(
                null,
                formSignUp.getFullname(),
                null,
                pathAvatar,
                null,
                null,
                null,
                null,
                null
        );

        Account account = accountService.registerAccount(registerAccount);
        createUser.setAccount(account);

        userService.save(createUser);

        FormGetUser formGetUser = modelMapper.map(createUser, FormGetUser.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "signup_complete", formGetUser)
        );
    }

    /**
     * Active account
     *
     * @param formActiveAccount
     * @return
     * @throws Exception
     */
    @PostMapping("/active-account")
    public ResponseEntity<MessageResponse> activeAccount(@Valid @RequestBody FormActiveAccount formActiveAccount) throws Exception {
        if (!accountService.existsAccountByUsername(formActiveAccount.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_user", null)
            );
        }

        Account accountFound = accountService.findAccountByUsername(formActiveAccount.getUsername());

        if (accountFound.getEnabled()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "account_already_confirmed", null)
            );
        }

        boolean checkCode = accountService.verifyAccount(formActiveAccount.getUsername(), formActiveAccount.getCode());

        if (!checkCode) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_validate_code", null)
            );
        }

        User user = userService.getByUsername(formActiveAccount.getUsername());
        ShopCart shopCartSaved = shopCartService.save(new ShopCart(null, new ArrayList<>()));
        user.setShopCart(shopCartSaved);
        user.setProductFavorites(new ArrayList<>());
        user.setVouchers(new ArrayList<>());
        user.setOrders(new ArrayList<>());
        User userSaved = userService.save(user);
        FormGetUser formGetUser = modelMapper.map(userSaved, FormGetUser.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "active_account_complete", formGetUser)
        );
    }

    /**
     * Send code to mail active account
     *
     * @param formSendCodeVerifyAccount
     * @return
     * @throws Exception
     */
    @PostMapping("/send-code")
    public ResponseEntity<MessageResponse> sendCodeVerifyAccount(@Valid @RequestBody FormSendCodeVerifyAccount formSendCodeVerifyAccount) throws Exception {
        if (!accountService.existsAccountByUsername(formSendCodeVerifyAccount.getUsername())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exist_user", null)
            );
        }

        Account accountFound = accountService.findAccountByUsername(formSendCodeVerifyAccount.getUsername());

        if (accountFound.getEnabled()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "account_already_confirmed", null)
            );
        }

        accountService.resendVerificationEmail(accountFound);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "send_code_to_mail_ok", null)
        );
    }
}

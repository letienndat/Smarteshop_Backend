package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.response.FormGetApp;
import com.smarteshop_backend.dto.response.FormRoleInApp;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app")
@Validated
public class AppController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Load home
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<MessageResponse> load() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            User user = userService.getByUsername(username);
            FormGetApp formGetApp = modelMapper.map(user, FormGetApp.class);
            List<FormRoleInApp> formRoleInApps = user.getAccount().getRoles()
                    .stream()
                    .map(role -> modelMapper.map(role, FormRoleInApp.class))
                    .toList();

            formGetApp.setFormRoleInApps(formRoleInApps);
            formGetApp.setNumberProductShopCart(user.getShopCart().getProductInShopCarts().size());
            formGetApp.setNumberProductFavorite(user.getProductFavorites().size());

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetApp)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "can_not_get_data_app", null)
            );
        }
    }
}

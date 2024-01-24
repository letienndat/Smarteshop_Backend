package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.Product;
import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.service.ProductService;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Add product in list favorite
     * @param idProduct
     * @return
     * @throws Exception
     */
    @PostMapping("/add-product-favorite")
    public ResponseEntity<MessageResponse> addProductFavoriteInUser(@Valid @RequestParam(name = "id") Long idProduct) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);

        boolean checkExistsProductInProductFavorites = userService.checkIdProductFavorite(idProduct);

        if (!checkExistsProductInProductFavorites) {
            try {
                Product product = productService.findById(idProduct);
                List<Product> products = user.getProductFavorites();
                products.add(product);
                userService.save(user);

                FormGetProductInFavorites formGetProductInFavorites = modelMapper.map(product, FormGetProductInFavorites.class);

//                FormGetApp formGetApp = modelMapper.map(user, FormGetApp.class);
//                List<FormRoleInApp> formRoleInApps = user.getAccount().getRoles()
//                        .stream()
//                        .map(role -> modelMapper.map(role, FormRoleInApp.class))
//                        .toList();
//
//                formGetApp.setFormRoleInApps(formRoleInApps);
//                formGetApp.setNumberProductShopCart(user.getShopCart().getProductInShopCarts().size());
//                formGetApp.setNumberProductFavorite(user.getProductFavorites().size());
//
//                messagingTemplate.convertAndSend("/topic/load-app", formGetApp);

                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.SUCCESS, "add_product_in_list_favorite_complete", formGetProductInFavorites)
                );
            } catch (Exception e) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "not_exists_product_id", null)
                );
            }
        }

        Product product = productService.findById(idProduct);
        List<Product> products = user.getProductFavorites();
        products.remove(product);
        userService.save(user);

        FormGetProductInFavorites formGetProductInFavorites = modelMapper.map(product, FormGetProductInFavorites.class);

//        FormGetApp formGetApp = modelMapper.map(user, FormGetApp.class);
//        List<FormRoleInApp> formRoleInApps = user.getAccount().getRoles()
//                .stream()
//                .map(role -> modelMapper.map(role, FormRoleInApp.class))
//                .toList();
//
//        formGetApp.setFormRoleInApps(formRoleInApps);
//        formGetApp.setNumberProductShopCart(user.getShopCart().getProductInShopCarts().size());
//        formGetApp.setNumberProductFavorite(user.getProductFavorites().size());
//
//        messagingTemplate.convertAndSend("/topic/load-app", formGetApp);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "remove_product_in_list_favorite_complete", formGetProductInFavorites)
        );
    }
}

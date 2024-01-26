package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddProductInShopCart;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.ProductInShopCartService;
import com.smarteshop_backend.service.ProductService;
import com.smarteshop_backend.service.ShopCartService;
import com.smarteshop_backend.service.SizeService;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/shop-cart")
@Validated
public class ShopCartController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductInShopCartService productInShopCartService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add product in shop cart
     * @param formAddProductInShopCart
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<MessageResponse> addProduct(@Valid @RequestBody FormAddProductInShopCart formAddProductInShopCart) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        boolean checkExistsProduct = shopCartService.checkProductAndSize(user.getShopCart().getId(), formAddProductInShopCart.getId(), formAddProductInShopCart.getSize());

        if (checkExistsProduct) {
            ProductInShopCart productInShopCartAddQuantity = shopCartService.addQuantity(
                    user.getShopCart().getId(),
                    formAddProductInShopCart.getId(),
                    formAddProductInShopCart.getSize(),
                    formAddProductInShopCart.getQuantity()
            );
            if (productInShopCartAddQuantity == null) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "add_product_in_shop_cart_fail", null)
                );
            }
            FormGetProductInShopCart formGetProductInShopCart = modelMapper.map(productInShopCartAddQuantity, FormGetProductInShopCart.class);
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "product_exists_in_shop_cart_and_add_quantity", formGetProductInShopCart)
            );
        }

        ShopCart shopCart = user.getShopCart();
        List<ProductInShopCart> productInShopCarts = shopCart.getProductInShopCarts();

        ProductInShopCart productInShopCart = new ProductInShopCart();
        try {
            Product productFind = productService.findById(formAddProductInShopCart.getId());
            productInShopCart.setProduct(productFind);
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_product_id", null)
            );
        }
        try {
            boolean checkSizeInProduct = productService.checkSizeInProduct(formAddProductInShopCart.getId(), formAddProductInShopCart.getSize());
            if (!checkSizeInProduct) {
                throw new  Exception();
            }
            Size sizeFind = sizeService.findBySize(formAddProductInShopCart.getSize());
            productInShopCart.setSize(sizeFind);
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_size", null)
            );
        }
        productInShopCart.setQuantity(formAddProductInShopCart.getQuantity());
        productInShopCart.setShopCart(shopCart);
        productInShopCart.setUpdateAt(new Date());
        productInShopCarts.add(productInShopCart);
        shopCartService.save(shopCart);

        FormGetProductInShopCart formGetProductInShopCart = modelMapper.map(productInShopCart, FormGetProductInShopCart.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "add_product_in_shop_cart_complete", formGetProductInShopCart)
        );
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getShopCart() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);

        ShopCart shopCart = user.getShopCart();
        FormGetShopCart formGetShopCart = modelMapper.map(shopCart, FormGetShopCart.class);

        List<FormProductInShopCart> formProductInShopCarts = user.getShopCart().getProductInShopCarts()
                .stream()
                .map(productInShopCart -> modelMapper.map(productInShopCart, FormProductInShopCart.class))
                .toList();
        formGetShopCart.setFormProductInShopCarts(formProductInShopCarts);
        formGetShopCart.setUserId(user.getId());
        formGetShopCart.setUsername(user.getAccount().getUsername());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetShopCart)
        );
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteProduct(@Valid @RequestParam(name = "id") Long id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        ShopCart shopCart = user.getShopCart();

        try {
            boolean statusRemove = shopCartService.removeProduct(shopCart.getId(), id);
            if (statusRemove) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.SUCCESS, "remove_product_in_shop_cart_complete", null)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "remove_product_in_shop_cart_fail", null)
            );
        }

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "remove_product_in_shop_cart_fail", null)
        );
    }
}

package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddOrder;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.ProductInShopCartService;
import com.smarteshop_backend.service.ShippingOptionService;
import com.smarteshop_backend.service.VoucherService;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@Validated
public class OrderController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductInShopCartService productInShopCartService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ShippingOptionService shippingOptionService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<MessageResponse> saveOrder(@Valid @RequestBody FormAddOrder formAddOrder) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        if (formAddOrder.getIdProductInShopCard().size() == 0) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_product_in_shop_cart", null)
            );
        }
        List<ProductInShopCart> productInShopCarts = productInShopCartService.findByIdIn(formAddOrder.getIdProductInShopCard());
        if (productInShopCarts.size() != formAddOrder.getIdProductInShopCard().size()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_id_product_in_shop_cart", null)
            );
        }
        Country country;
        try {
            country = Country.valueOf(formAddOrder.getCountry().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_country_name", null)
            );
        }
        ShippingOption shippingOption;
        try {
            shippingOption = shippingOptionService.findById(formAddOrder.getIdShippingOption());
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_shipping_option_id", null)
            );
        }
        PaymentOption paymentOption;
        try {
            paymentOption = PaymentOption.valueOf(formAddOrder.getPaymentOption().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_payment_option_name", null)
            );
        }
        Voucher voucher;
        try {
            voucher = voucherService.findByCode(formAddOrder.getVoucher());
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_voucher_code", null)
            );
        }

        Date currentDate = new Date();
        Double subTotal = shippingOption.getPrice();
        Double amountReduced = 0.0;
        for (ProductInShopCart product : productInShopCarts) {
            double price = product.getProduct().getPrice() * product.getQuantity();
            double discount = price * product.getProduct().getDiscount() * 0.01;
            subTotal += price - discount;

            if (voucher.getExpirationDate().before(currentDate)) break;

            List<Long> idBrandApply = voucher.getBrandsApply().stream().map(Brand::getId).toList();
            List<Long> idCategoryApply = voucher.getCategoriesApply().stream().map(Category::getId).toList();
            if (idBrandApply.contains(product.getProduct().getBrand().getId()) || idCategoryApply.contains(product.getProduct().getCategory().getId())) {
                if (product.getProduct().getPrice() < voucher.getMinPriceApply()) {
                    break;
                }
                double expectedDiscount = price * voucher.getPercentDiscount() * 0.01;
                double maxPriceDiscount = voucher.getMaxPriceDiscount() * product.getQuantity();
                amountReduced += expectedDiscount > maxPriceDiscount ? maxPriceDiscount : expectedDiscount;
            }
        }
        Double totalPayment = subTotal - amountReduced;

        Order order = new Order(
                null,
                user,
                formAddOrder.getFullname(),
                formAddOrder.getProvince(),
                formAddOrder.getPostalCode(),
                country,
                shippingOption,
                null,
                paymentOption,
                voucher,
                subTotal,
                amountReduced,
                totalPayment,
                currentDate
        );

        List<ProductInOrder> productInOrders = new ArrayList<>();
        productInShopCarts.forEach(productInShopCart -> {
            ProductInOrder product = new ProductInOrder(
                    null,
                    productInShopCart.getProduct(),
                    productInShopCart.getSize(),
                    productInShopCart.getQuantity(),
                    order
            );
            productInOrders.add(product);
        });
        order.setProductInOrders(productInOrders);
        user.getOrders().add(order);

        if (formAddOrder.isSaveBillingAddress()) {
            BillingAddress billingAddress = new BillingAddress(
                    null,
                    order.getFullname(),
                    order.getProvince(),
                    order.getPostalCode(),
                    country,
                    user
            );
            user.getBillingAddresses().add(billingAddress);
        }
        userService.save(user);
        productInShopCartService.removeByIds(formAddOrder.getIdProductInShopCard());

        FormGetOrder formGetOrder = modelMapper.map(order, FormGetOrder.class);
        List<FormGetProductInOrder> productInOrderList = order.getProductInOrders()
                .stream()
                .map(productInOrder -> modelMapper.map(productInOrder, FormGetProductInOrder.class))
                .toList();
        formGetOrder.setFormGetProductInOrders(productInOrderList);
        formGetOrder.setCountry(country.getDisplayName());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "save_order_complete", formGetOrder)
        );
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getOrder(@RequestParam(name = "id", required = false) Long id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<Order> orders = user.getOrders();

        if (id == null) {
            FormGetOrders formGetOrders = new FormGetOrders(
                    user.getId(),
                    user.getAccount().getUsername(),
                    null
            );
            List<FormGetOrder> formGetOrderList = orders.stream()
                    .map(order -> {
                        List<FormGetProductInOrder> formGetProductInOrders = order.getProductInOrders()
                                .stream()
                                .map(productInOrder -> modelMapper.map(productInOrder, FormGetProductInOrder.class))
                                .toList();
                        FormGetOrder formGetOrder = modelMapper.map(order, FormGetOrder.class);
                        formGetOrder.setFormGetProductInOrders(formGetProductInOrders);

                        return formGetOrder;
                    })
                    .toList();
            formGetOrders.setFormGetOrders(formGetOrderList);

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetOrders)
            );
        }

        for (Order order : orders) {
            if (order.getId().equals(id)) {
                FormGetOrder formGetOrder = modelMapper.map(order, FormGetOrder.class);
                List<FormGetProductInOrder> formGetProductInOrders = order.getProductInOrders()
                        .stream()
                        .map(productInOrder -> modelMapper.map(productInOrder, FormGetProductInOrder.class))
                        .toList();
                formGetOrder.setFormGetProductInOrders(formGetProductInOrders);

                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetOrder)
                );
            }
        }

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.FALD, "not_exists_order_id", null)
        );
    }
}

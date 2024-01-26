package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddBillingAddress;
import com.smarteshop_backend.dto.request.FormUpdateBillingAddress;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.BillingAddressService;
import com.smarteshop_backend.service.ProductService;
import com.smarteshop_backend.service.VoucherService;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private VoucherService voucherService;

    @Autowired
    private BillingAddressService billingAddressService;

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

    @GetMapping("/get-favorites")
    public ResponseEntity<MessageResponse> getFavoritesList() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<Product> productFavorites = user.getProductFavorites();

        FormGetFavorites formGetFavorites = modelMapper.map(user, FormGetFavorites.class);
        List<FormGetProductInFavorites> formGetProductInFavorites = productFavorites.stream()
                .map(product -> modelMapper.map(product, FormGetProductInFavorites.class))
                .toList();

        formGetFavorites.setFormGetProductInFavorites(formGetProductInFavorites);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetFavorites)
        );
    }

    @GetMapping("/get-vouchers")
    public ResponseEntity<MessageResponse> getVouchersList() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        FormGetVouchers formGetVouchers = modelMapper.map(user, FormGetVouchers.class);

        List<FormGetVoucher> vouchers = user.getVouchers().stream()
                .map(voucher -> modelMapper.map(voucher, FormGetVoucher.class))
                .toList();
        formGetVouchers.setFormGetVouchers(vouchers);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete" , formGetVouchers)
        );
    }

    @PostMapping("/add-voucher")
    public ResponseEntity<MessageResponse> addVoucher(@Valid @RequestParam(name = "code") String code) throws Exception {
        code = code.trim();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);

        int checkVoucher = voucherService.checkVoucher(user.getId(), code);
        if (checkVoucher == 0) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_voucher_code", null)
            );
        }
        if (checkVoucher == 2) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "voucher_code_already_exists_in_list_voucher", null)
            );
        }
        Voucher voucher = voucherService.findByCode(code);
        user.getVouchers().add(voucher);
        userService.save(user);
        FormGetVoucher formGetVoucher = modelMapper.map(voucher, FormGetVoucher.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.FALD, "add_voucher_in_list_voucher_complete", formGetVoucher)
        );
    }

    @DeleteMapping("/remove-voucher")
    public ResponseEntity<MessageResponse> removeVoucher(@Valid @RequestParam(name = "code") String code) throws Exception {
        code = code.trim();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);

        int checkVoucher = voucherService.checkVoucher(user.getId(), code);
        if (checkVoucher == 0) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_voucher_code", null)
            );
        }
        if (checkVoucher == 1) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "voucher_code_not_exists_in_list_voucher", null)
            );
        }
        Voucher voucher = voucherService.findByCode(code);
        user.getVouchers().remove(voucher);
        userService.save(user);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.FALD, "remove_voucher_in_list_voucher_complete", null)
        );
    }

    @GetMapping("/get-billing-address")
    public ResponseEntity<MessageResponse> getBillingAddress(@Valid @RequestParam(name = "id", required = false) Long id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<BillingAddress> billingAddresses = user.getBillingAddresses();
        if (id == null) {
            FormGetBillingAddresses formGetBillingAddresses = modelMapper.map(user, FormGetBillingAddresses.class);
            List<FormGetBillingAddress> formGetBillingAddresses1 = billingAddresses.stream()
                    .map(billingAddress -> modelMapper.map(billingAddress, FormGetBillingAddress.class))
                    .toList();
            formGetBillingAddresses.setFormGetBillingAddresses(formGetBillingAddresses1);

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetBillingAddresses)
            );
        }
        BillingAddress billingAddress = null;
        boolean checkExistsBillingAddress = false;
        for (BillingAddress billingAddress1 : billingAddresses) {
            if (billingAddress1.getId().equals(id)) {
                checkExistsBillingAddress = true;
                billingAddress = billingAddress1;
                break;
            }
        }
        if (!checkExistsBillingAddress) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_billing_address", null)
            );
        }
        FormGetBillingAddress formGetBillingAddress = modelMapper.map(billingAddress, FormGetBillingAddress.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetBillingAddress)
        );
    }

    @PostMapping("/save-billing-address")
    public ResponseEntity<MessageResponse> saveBillingAddress(@Valid @RequestBody FormAddBillingAddress formAddBillingAddress) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<BillingAddress> billingAddresses = user.getBillingAddresses();
        Country country = null;
        try {
            country = Country.valueOf(formAddBillingAddress.getCountry().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_country_name", null)
            );
        }
        BillingAddress billingAddress = new BillingAddress(
                null,
                formAddBillingAddress.getFullname(),
                formAddBillingAddress.getProvince(),
                formAddBillingAddress.getPostalCode(),
                country,
                user
        );
        billingAddresses.add(billingAddress);
        userService.save(user);
        FormGetBillingAddress formGetBillingAddress = modelMapper.map(billingAddress, FormGetBillingAddress.class);
        formGetBillingAddress.setCountry(country.getDisplayName());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "save_billing_address_complete", formGetBillingAddress)
        );
    }

    @PutMapping("/update-billing-address")
    public ResponseEntity<MessageResponse> updateBillingAddress(@Valid @RequestBody FormUpdateBillingAddress formUpdateBillingAddress) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<BillingAddress> billingAddresses = user.getBillingAddresses();
        BillingAddress billingAddress = null;
        boolean checkExistsBillingAddress = false;
        for (BillingAddress billingAddress1 : billingAddresses) {
            if (billingAddress1.getId().equals(formUpdateBillingAddress.getId())) {
                checkExistsBillingAddress = true;
                billingAddress = billingAddress1;
                break;
            }
        }
        if (!checkExistsBillingAddress) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_billing_address", null)
            );
        }
        Country country = null;
        try {
            country = Country.valueOf(formUpdateBillingAddress.getCountry().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_country_name", null)
            );
        }
        billingAddress.setFullname(formUpdateBillingAddress.getFullname());
        billingAddress.setProvince(formUpdateBillingAddress.getProvince());
        billingAddress.setPostalCode(formUpdateBillingAddress.getPostalCode());
        billingAddress.setCountry(country);
        userService.save(user);

        FormGetBillingAddress formGetBillingAddress = modelMapper.map(billingAddress, FormGetBillingAddress.class);
        formGetBillingAddress.setCountry(country.getDisplayName());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "update_billing_address_complete", formGetBillingAddress)
        );
    }

    @DeleteMapping("/remove-billing-address")
    public ResponseEntity<MessageResponse> removeBillingAddress(@Valid @RequestParam(name = "id") Long id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<BillingAddress> billingAddresses = user.getBillingAddresses();
        BillingAddress billingAddress = null;
        boolean checkExistsBillingAddress = false;

        for (BillingAddress billingAddress1 : billingAddresses) {
            if (billingAddress1.getId().equals(id)) {
                checkExistsBillingAddress = true;
                billingAddress = billingAddress1;
                break;
            }
        }
        if (!checkExistsBillingAddress) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_billing_address", null)
            );
        }
        billingAddressService.removeBillingAddress(id);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "remove_billing_address_complete", null)
        );
    }
}

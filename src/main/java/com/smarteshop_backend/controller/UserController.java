package com.smarteshop_backend.controller;

import com.smarteshop_backend.const_app.ConstApp;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.*;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import com.smarteshop_backend.util.FileStorageService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Value("${app.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PutMapping("/change-avatar")
    public ResponseEntity<MessageResponse> changeAvatar(
            @Valid @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);

        String pathAvatarLocal;
        if (avatar != null) {
            try {
                pathAvatarLocal = fileStorageService.storeFile(avatar, ConstApp.PREFIX_PATH_AVATAR);
            } catch (IOException e) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "change_avatar_fail", null)
                );
            }
            boolean checkAvatarDefault = user.getPathAvatar().endsWith("/avatars/default.png");

            if (!checkAvatarDefault) {
                try {
                    Path filePath = Paths.get(uploadDir + "/" + user.getPathAvatar());
                    Files.delete(filePath);
                } catch (Exception e) {
                    return ResponseEntity.ok(
                            new MessageResponse(TypeMessage.FALD, "change_avatar_fail", null)
                    );
                }
            }
        } else {
            boolean checkAvatarDefault = user.getPathAvatar().endsWith("/avatars/default.png");

            if (!checkAvatarDefault) {
                try {
                    Path filePath = Paths.get(uploadDir + "/" + user.getPathAvatar());
                    Files.delete(filePath);
                } catch (Exception e) {
                    return ResponseEntity.ok(
                            new MessageResponse(TypeMessage.FALD, "remove_avatar_fail", null)
                    );
                }
            }
            pathAvatarLocal = "/" + ConstApp.PREFIX_PATH_AVATAR + "/" + ConstApp.PREFIX_PATH_DEFAULT;
        }

        user.setPathAvatar(pathAvatarLocal);
        userService.save(user);

        return ResponseEntity.ok(
                new MessageResponse(
                        TypeMessage.SUCCESS,
                        avatar == null ? "remove_avatar_complete" : "change_avatar_complete",
                        null
                )
        );
    }

    /**
     * Add product in list favorite
     *
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
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetVouchers)
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
}

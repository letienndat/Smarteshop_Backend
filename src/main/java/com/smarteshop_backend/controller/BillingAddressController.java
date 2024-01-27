package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddBillingAddress;
import com.smarteshop_backend.dto.request.FormUpdateBillingAddress;
import com.smarteshop_backend.dto.response.FormGetBillingAddress;
import com.smarteshop_backend.dto.response.FormGetBillingAddresses;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.BillingAddress;
import com.smarteshop_backend.entity.Country;
import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.service.BillingAddressService;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing-address")
@Validated
public class BillingAddressController {
    @Autowired
    private BillingAddressService billingAddressService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get billing address
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping
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

    /**
     * Save billing address
     *
     * @param formAddBillingAddress
     * @return
     * @throws Exception
     */
    @PostMapping
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

    /**
     * Update billing address
     *
     * @param formUpdateBillingAddress
     * @return
     * @throws Exception
     */
    @PutMapping
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

    /**
     * Remove billing address
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping
    public ResponseEntity<MessageResponse> removeBillingAddress(@Valid @RequestParam(name = "id") Long id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username);
        List<BillingAddress> billingAddresses = user.getBillingAddresses();
        boolean checkExistsBillingAddress = false;

        for (BillingAddress billingAddress1 : billingAddresses) {
            if (billingAddress1.getId().equals(id)) {
                checkExistsBillingAddress = true;
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

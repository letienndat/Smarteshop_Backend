package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddShippingOption;
import com.smarteshop_backend.dto.response.FormGetShippingOption;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.ShippingOption;
import com.smarteshop_backend.service.ShippingOptionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/shipping-option")
@Validated
public class ShippingOptionController {
    @Autowired
    private ShippingOptionService shippingOptionService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MessageResponse> addShippingOption(@Valid @RequestBody FormAddShippingOption formAddShippingOption) {
        String name = formAddShippingOption.getName().trim();
        boolean checkName = shippingOptionService.checkName(name);
        if (checkName) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exists_shipping_option_name", null)
            );
        }
        ShippingOption shippingOption = new ShippingOption(
                null, formAddShippingOption.getName(),
                formAddShippingOption.getPrice()
        );
        ShippingOption shippingOptionSaved = shippingOptionService.save(shippingOption);
        FormGetShippingOption formGetShoppingOption = modelMapper.map(
                shippingOptionSaved,
                FormGetShippingOption.class
        );

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "add_shopping_option_complete", formGetShoppingOption)
        );
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getShippingOptions() {
        List<FormGetShippingOption> formGetShippingOptions = shippingOptionService.findAll()
                .stream()
                .map(shippingOption -> modelMapper.map(shippingOption, FormGetShippingOption.class))
                .toList();

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "response_complete", formGetShippingOptions)
        );
    }
}

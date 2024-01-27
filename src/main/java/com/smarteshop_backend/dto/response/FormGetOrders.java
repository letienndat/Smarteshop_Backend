package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormGetOrders {
    private Long userId;
    private String username;
    private List<FormGetOrder> formGetOrders;
}

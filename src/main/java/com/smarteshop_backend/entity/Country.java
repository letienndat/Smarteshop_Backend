package com.smarteshop_backend.entity;

import lombok.*;

@RequiredArgsConstructor
public enum Country {
    VIETNAM("Vietnam"),
    USA("United States"),
    CANADA("Canada"),
    INDIA("India"),
    AUSTRALIA("Australia"),
    AFGHANISTAN("Afghanistan"),
    ALBANIA("Albania"),
    ALGERIA("Algeria"),
    ANDORRA("Andorra"),
    ANGOLA("Angola");

    @Getter
    private final String displayName;

//    public static void main(String[] args) {
//        // Sử dụng enum
//        for (Country country : Country.values()) {
//            System.out.println(country.name() + ": " + country.getDisplayName());
//        }
//    }
}


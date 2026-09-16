package com.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CustomerRequest(

        Long customerId,

        @NotBlank
        String customerName,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String phone,

        @NotBlank
        String address,

        LocalDate dateOfBirth

) {
}
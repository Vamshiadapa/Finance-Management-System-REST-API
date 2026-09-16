package com.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CustomerRequest(

        @NotBlank(message = "Customer name is required")
        String customerName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[6-9][0-9]{9}$",
                message = "Phone number must be 10 digits and start with 6, 7, 8, or 9"
        )
        String phone,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Date of birth is required")
        LocalDate dateOfBirth

) {
}
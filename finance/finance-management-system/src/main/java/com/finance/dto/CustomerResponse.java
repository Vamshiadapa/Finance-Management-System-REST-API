package com.finance.dto;

import java.time.LocalDate;

public record CustomerResponse(

        Long customerId,

        String customerName,

        String email,

        String phone,

        String address,

        LocalDate dateOfBirth

) {
}
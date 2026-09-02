package com.example.shoppingcart.dto;

import com.example.shoppingcart.enums.CustomerAction;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerAuthRequest {

    private CustomerAction action;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain numbers not Alphabets")
    private String phoneNumber;
}
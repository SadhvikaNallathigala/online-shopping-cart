package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CustomerAuthRequest;
import com.example.shoppingcart.dto.CustomerResponse;
import com.example.shoppingcart.entity.Customer;
import com.example.shoppingcart.entity.CustomerHistory;
import com.example.shoppingcart.enums.CustomerAction;
import com.example.shoppingcart.repository.CustomerHistoryRepository;
import com.example.shoppingcart.repository.CustomerRepository;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.response.MetaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.shoppingcart.enums.CustomerAction;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerHistoryRepository customerHistoryRepository;

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    public ApiResponse<CustomerResponse> registerOrLogin(
            CustomerAuthRequest request) {

        CustomerAction action = request.getAction();

        switch (action) {
            case REGISTER:
                return register(request);

            case LOGIN:
                return login(request);

            default:
                throw new IllegalArgumentException(
                        "Invalid customer action");
        }
    }

    private ApiResponse<CustomerResponse> register(
            CustomerAuthRequest request) {

        if (customerRepository.existsByEmail(
                request.getEmail())) {

            throw new IllegalArgumentException(
                    "Customer with this email already exists");
        }

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPassword(request.getPassword());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setDeleted(false);

        LocalDateTime now = LocalDateTime.now(IST);

        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        Customer savedCustomer =
                customerRepository.save(customer);

        saveHistory(
                savedCustomer.getCustomerId(),
                CustomerAction.REGISTER.name());

        CustomerResponse response =
                convertToResponse(savedCustomer);

        MetaResponse meta = new MetaResponse(
                LocalDateTime.now(IST),
                "Customer registered successfully");

        return new ApiResponse<>(
                true,
                response,
                null,
                meta);
    }

    private ApiResponse<CustomerResponse> login(
            CustomerAuthRequest request) {

        Optional<Customer> optionalCustomer =
                customerRepository.findByEmail(
                        request.getEmail());

        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        Customer customer = optionalCustomer.get();

        if (customer.isDeleted()) {
            throw new IllegalArgumentException(
                    "Customer account is deleted");
        }

        if (!customer.getPassword()
                .equals(request.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        saveHistory(
                customer.getCustomerId(),
                CustomerAction.LOGIN.name());

        CustomerResponse response =
                convertToResponse(customer);

        MetaResponse meta = new MetaResponse(
                LocalDateTime.now(IST),
                "Login successful");

        return new ApiResponse<>(
                true,
                response,
                null,
                meta);
    }

    private void saveHistory(
            UUID customerId,
            String action) {

        CustomerHistory history =
                new CustomerHistory();

        history.setCustomerId(customerId);
        history.setAction(action);
        history.setTimestamp(
                LocalDateTime.now(IST));

        customerHistoryRepository.save(history);
    }

    private CustomerResponse convertToResponse(
            Customer customer) {

        CustomerResponse response =
                new CustomerResponse();

        response.setCustomerId(
                customer.getCustomerId());

        response.setName(
                customer.getName());

        response.setEmail(
                customer.getEmail());

        response.setPhoneNumber(
                customer.getPhoneNumber());

        return response;
    }

    public ApiResponse<Page<CustomerHistory>> getHistory(
            UUID customerId,
            Integer page,
            Integer size) {

        page = page == null ? 0 : page;
        size = size == null ? 10 : size;

        Page<CustomerHistory> history =
                customerHistoryRepository
                        .findByCustomerIdOrderByTimestampDesc(
                                customerId,
                                PageRequest.of(page, size));

        return new ApiResponse<>(
                true,
                history,
                null,
                new MetaResponse(
                        LocalDateTime.now(IST),
                        "Customer history retrieved successfully"));
    }
}
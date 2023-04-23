package com.cham.customercore.controller;

import com.cham.customercore.domain.CustomerCore;
import com.cham.customercore.service.CustomerCoreService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class CustomerCoreController {
    @Autowired
    private CustomerCoreService customerCoreService;
    @PostMapping("/api/customer-core")
    @ResponseStatus(HttpStatus.OK)
    public String createCustomer(@RequestBody CustomerCore customerCore) {
        log.info("Inside CustomerCoreController.createCustomer()", customerCore.toString());
        String customerId = customerCoreService.saveCustomer(customerCore);
        return customerId;
    }
    @GetMapping("/api/customer-core")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerCore> findAllCustomers(){
        log.info("Inside CustomerCoreController.findAllCustomers()");
        return customerCoreService.findAllCustomers();
    }

    @GetMapping("/api/customer-core/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CustomerCore> findCustomerById(@PathVariable String id){
        log.info("Inside CustomerCoreController.findCustomerById()");
        return customerCoreService.findCustomerById(id);
    }

    @GetMapping("/api/customer-core/ssn")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CustomerCore> findCustomerBySsn(@RequestParam(required = true) String ssn){
        log.info("Inside CustomerCoreController.findCustomerBySsn()",ssn);
        return customerCoreService.findCustomerBySsn(ssn);
    }

    @GetMapping("/api/customer-core/first-name")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CustomerCore> findCustomerByFirstName(@RequestParam(required = true) String firstName){
        log.info("Inside CustomerCoreController.findCustomerByFirstName()",firstName);
        return customerCoreService.findCustomerByFirstName(firstName);
    }
}

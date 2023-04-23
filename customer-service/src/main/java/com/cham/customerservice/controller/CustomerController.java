package com.cham.customerservice.controller;

import com.cham.customerservice.client.CustomerCoreClient;
import com.cham.customerservice.domain.CustomerCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerCoreClient customerCoreClient;
    @GetMapping("/api/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerCore> findAllCustomersFromCoreAPI(){
        log.info("Inside CustomerController.findAllCustomersFromCoreAPI()");
        List<CustomerCore> customerList = customerCoreClient.findAllCustomers();
        customerList.stream().forEach(t -> log.info(t.toString()));
        return customerList;
    }

}

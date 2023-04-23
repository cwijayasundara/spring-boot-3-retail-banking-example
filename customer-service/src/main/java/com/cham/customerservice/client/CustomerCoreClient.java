package com.cham.customerservice.client;

import com.cham.customerservice.domain.CustomerCore;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "customer-core")
public interface CustomerCoreClient {

    @GetMapping("/api/customer-core")
    List<CustomerCore> findAllCustomers();

}

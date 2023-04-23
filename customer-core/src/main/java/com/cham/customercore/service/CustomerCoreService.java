package com.cham.customercore.service;

import com.cham.customercore.domain.Audit;
import com.cham.customercore.domain.CustomerCore;
import com.cham.customercore.producer.AuditMessageProducer;
import com.cham.customercore.repository.CustomerCoreRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCoreService {

    @Autowired
    private CustomerCoreRepository customerCoreRepository;

    @Autowired
    private AuditMessageProducer auditMessageProducer;

    @Value("${test.topic}")
    private String topic;

    private Gson gson = new Gson();

    public Optional<CustomerCore> findCustomerById(String id){
        Optional<CustomerCore> customer;
        try {
            customer = customerCoreRepository.findById(id);
        }catch(DataAccessException e){
            log.error("Exception while accessing Redis", e);
            throw  new RuntimeException("Exception while accessing Redis",e);
        }
        return customer;
    }

    public Optional<CustomerCore> findCustomerByFirstName(String firstName){
        Optional<CustomerCore> customer;
        try {
            customer = customerCoreRepository.findByFirstName(firstName);
        }catch(DataAccessException e){
            log.error("Exception while accessing Redis", e);
            throw  new RuntimeException("Exception while accessing Redis",e);
        }
        return customer;
    }

    public Optional<CustomerCore> findCustomerBySsn(String ssn){
        Optional<CustomerCore> customer;
        try {
            customer = customerCoreRepository.findBySsn(ssn);
        }catch(DataAccessException e){
            log.error("Exception while accessing Redis", e);
            throw  new RuntimeException("Exception while accessing Redis",e);
        }
        return customer;
    }
    public List<CustomerCore> findAllCustomers(){
       List<CustomerCore> customerList;
        try {
            customerList = customerCoreRepository.findAll();
        }catch(DataAccessException e){
            log.error("Exception while accessing Redis", e);
            throw  new RuntimeException("Exception while accessing Redis",e);
        }
        return customerList;
    }

    public String saveCustomer(CustomerCore customerCore){
        CustomerCore returnedCustomer;
        try{
            returnedCustomer = customerCoreRepository.save(customerCore);
        }catch(DataAccessException e){
            log.error("Exception while accessing Redis", e);
            throw  new RuntimeException("Exception while accessing Redis",e);
        }
        Audit auditMessage = Audit
                .builder()
                .id(10L)
                .auditEvent("CUSTOMER CREATED")
                .auditEventPayload(returnedCustomer.toString())
                .build();
        auditMessageProducer.send(topic, gson.toJson(auditMessage));
        return returnedCustomer.getId();
    }
}

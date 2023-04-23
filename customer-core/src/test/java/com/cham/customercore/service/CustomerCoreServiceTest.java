package com.cham.customercore.service;

import com.cham.customercore.domain.CustomerCore;
import com.cham.customercore.producer.AuditMessageProducer;
import com.cham.customercore.repository.CustomerCoreRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class CustomerCoreServiceTest {

    private CustomerCoreRepository customerCoreRepositoryMock;

    private AuditMessageProducer auditMessageProducerMock;

    private CustomerCoreService customerCoreService;

    private Gson gson = new Gson();

    CustomerCore rob = CustomerCore
            .builder()
            .id("12345679")
            .ssn("ssn-0023")
            .accountNumber("9876534")
            .firstName("Rob")
            .lastName("Atkins")
            .build();

    @BeforeEach
    void setUp() {
        customerCoreRepositoryMock = mock(CustomerCoreRepository.class);
        auditMessageProducerMock = mock(AuditMessageProducer.class);
        customerCoreService = new CustomerCoreService(customerCoreRepositoryMock,auditMessageProducerMock, "test.topic",gson);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findCustomerById() {
        doThrow(new DataAccessException("Exception while accessing Redis"){}).when(customerCoreRepositoryMock).findById(anyString());
        assertThrows(RuntimeException.class, () -> {
            customerCoreService.findCustomerById("any-string");
        });
    }

    @Test
    void findCustomerByFirstName() {
        doThrow(new DataAccessException("Exception while accessing Redis"){}).when(customerCoreRepositoryMock).findByFirstName(anyString());
        assertThrows(RuntimeException.class, () -> {
            customerCoreService.findCustomerByFirstName("any-string");
        });
    }

    @Test
    void findCustomerBySsn() {
        doThrow(new DataAccessException("Exception while accessing Redis"){}).when(customerCoreRepositoryMock).findBySsn(anyString());
        assertThrows(RuntimeException.class, () -> {
            customerCoreService.findCustomerBySsn("any-string");
        });
    }

    @Test
    void findAllCustomers() {
        doThrow(new DataAccessException("Exception while accessing Redis"){}).when(customerCoreRepositoryMock).findAll();
        assertThrows(RuntimeException.class, () -> {
            customerCoreService.findAllCustomers();
        });
    }

    @Test
    void saveCustomer() {
        doThrow(new DataAccessException("Exception while accessing Redis"){}).when(customerCoreRepositoryMock).save(rob);
        assertThrows(RuntimeException.class, () -> {
            customerCoreService.saveCustomer(rob);
        });
    }
}
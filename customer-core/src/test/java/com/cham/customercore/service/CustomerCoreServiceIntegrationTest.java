package com.cham.customercore.service;

import com.cham.customercore.TestRedisConfiguration;
import com.cham.customercore.domain.CustomerCore;
import com.cham.customercore.repository.CustomerCoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("disabled test")
@ActiveProfiles({ "test"})
@SpringBootTest(classes = TestRedisConfiguration.class)
class CustomerCoreServiceIntegrationTest {
    @Autowired
    RedisOperations<Object, Object> operations;
    @Autowired
    CustomerCoreRepository customerCoreRepository;
    private RedisServer redisServer;
    CustomerCore rob = CustomerCore
            .builder()
            .id("12345679")
            .ssn("ssn-0023")
            .accountNumber("9876534")
            .firstName("Rob")
            .lastName("Atkins")
            .build();
    CustomerCore tony = CustomerCore
            .builder()
            .id("456789032")
            .ssn("ssn-987654")
            .accountNumber("67654387")
            .firstName("Tony")
            .lastName("Stark")
            .build();

    @BeforeEach
    @AfterEach
    void setUp()  {
        redisServer = new RedisServer(6370);
        redisServer.start();
        operations.execute((RedisConnection connection) -> {
            connection.flushDb();
            return "OK";
        });
        flushTestUsers();
    }

    @AfterEach
    void tearDown() {
        redisServer.stop();
    }

    @Test
    void findCustomerById() {
        Optional<CustomerCore> customer = customerCoreRepository.findById("12345679");
        assertThat(customer.get().getId().equals("12345679"));
        assertThat(customer.get().getLastName().equals("Atkins"));
        assertThat(customer.get().getAccountNumber().equals("9876534"));
    }

    @Test
    void findCustomerByFirstName() {
        Optional<CustomerCore> customer = customerCoreRepository.findByFirstName("Tony");
        assertThat(customer.get().getId().equals("456789032"));
        assertThat(customer.get().getFirstName().equals("Tony"));
        assertThat(customer.get().getSsn().equals("ssn-987654"));
    }

    @Test
    void findCustomerBySsn() {
        Optional<CustomerCore> customer = customerCoreRepository.findBySsn("ssn-0023");
        assertThat(customer.get().getId().equals("12345679"));
        assertThat(customer.get().getLastName().equals("Atkins"));
        assertThat(customer.get().getAccountNumber().equals("9876534"));
    }

    @Test
    void findAllCustomers() {
        List<CustomerCore> customers = customerCoreRepository.findAll();
        assertThat(customers.size()==2);
        CustomerCore firstCustomer = customers.get(0);
        assertThat(firstCustomer.getId().equals("12345679"));
        assertThat(firstCustomer.getLastName().equals("Atkins"));
        assertThat(firstCustomer.getAccountNumber().equals("9876534"));
    }

    @Test
    void saveCustomer() {
        CustomerCore customer = customerCoreRepository.save(tony);
        assertThat(customer.getId().equals("456789032"));
        assertThat(customer.getFirstName().equals("Tony"));
        assertThat(customer.getSsn().equals("ssn-987654"));
    }
    private void flushTestUsers() {
        customerCoreRepository.saveAll(Arrays.asList(rob,tony));
    }
}
package com.cham.customercore.controller;

import com.cham.customercore.TestRedisConfiguration;
import com.cham.customercore.domain.CustomerCore;
import com.cham.customercore.repository.CustomerCoreRepository;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.net.URL;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Slf4j
class CustomerCoreControllerIntegrationTest {

    @LocalServerPort
    @Getter
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CustomerCoreRepository customerCoreRepository;
    @Value("${test.topic}")
    private String topic;
    private final Gson gson = new Gson();
    @Autowired
    private KafkaTemplate kafkaTemplate;
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
    void setUp() {
        customerCoreRepository.deleteAll();
        customerCoreRepository.saveAll(Arrays.asList(rob,tony));
    }

    @AfterEach
    void tearDown() {
        customerCoreRepository.deleteAll();
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void createCustomer(CapturedOutput capturedOutput) throws Exception{
        URL baseUrl = new URL("http://localhost:" + port + "/api/customer-core");
        ResponseEntity<String> responseEntity = testRestTemplate
                .postForEntity(baseUrl.toString(), tony, String.class);
        log.info("The response is " + responseEntity.getBody());
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.getBody().contains("456789032"));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertTrue(capturedOutput.getOut().contains("sending payload for audit topic"));
        assertTrue(capturedOutput.getOut().contains("message sent to the audit topic"));
    }

    @Test
    void findAllCustomers() throws Exception {
        URL baseUrl = new URL("http://localhost:" + port + "/api/customer-core");
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
        log.info("The response is " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().contains("456789032"));
        assertTrue(response.getBody().contains("ssn-987654"));
        assertTrue(response.getBody().contains("67654387"));
        assertTrue(response.getBody().contains("Tony"));
        assertTrue(response.getBody().contains("Stark"));
    }

    @Test
    void findCustomerById() throws Exception {
        URL baseUrl = new URL("http://localhost:" + port + "/api/customer-core/12345679");
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
        log.info("The response is " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().contains("12345679"));
        assertTrue(response.getBody().contains("ssn-0023"));
        assertTrue(response.getBody().contains("9876534"));
        assertTrue(response.getBody().contains("Rob"));
        assertTrue(response.getBody().contains("Atkins"));
    }

    @Test
    void findCustomerBySsn() throws Exception {
        URL baseUrl = new URL("http://localhost:" + port + "/api/customer-core/ssn?ssn=ssn-987654");
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
        log.info("The response is " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().contains("ssn-987654"));
        assertTrue(response.getBody().contains("67654387"));
        assertTrue(response.getBody().contains("Tony"));
        assertTrue(response.getBody().contains("Stark"));
    }

    @Test
    void findCustomerByFirstName() throws Exception {
        URL baseUrl = new URL("http://localhost:" + port + "/api/customer-core/first-name?firstName=Tony");
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
        log.info("The response is " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().contains("ssn-987654"));
        assertTrue(response.getBody().contains("67654387"));
        assertTrue(response.getBody().contains("Tony"));
        assertTrue(response.getBody().contains("Stark"));
    }
}
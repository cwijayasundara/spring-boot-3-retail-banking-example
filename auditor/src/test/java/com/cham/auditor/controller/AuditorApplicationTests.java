package com.cham.auditor.controller;

import com.cham.auditor.domain.Audit;
import com.cham.auditor.repository.AuditRepository;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Slf4j
class AuditorApplicationTests {

	@LocalServerPort
	@Getter
	private int port;
	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private AuditRepository auditRepository;
	@Value("${test.topic}")
	private String topic;
	private final Gson gson = new Gson();
	@Autowired
	private KafkaTemplate kafkaTemplate;
	LocalDateTime now = LocalDateTime.now();

	@BeforeEach
	void setUp() {
		Audit auditRecord1 = auditRecordFactory(1L, "customer created",
				"test customer1 created");
		Audit auditRecord2 = auditRecordFactory(2L, "customer created",
				"test customer2 created");
		auditRepository.deleteAll();
		auditRepository.saveAll(Arrays.asList(auditRecord1,auditRecord2));
	}

	@AfterEach
	void cleanUp(){
		auditRepository.deleteAll();
	}

	@Test
	@ExtendWith(OutputCaptureExtension.class)
	void shouldTestMessageConsumed(CapturedOutput capturedOutput){
		String testAudit = gson.toJson(auditRecordFactory(3L,"account created",
				"test account created" ));
		kafkaTemplate.send(topic,testAudit); // produce the message to the Kafka topic
        // wait before reading from the DB as its async
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		assertTrue(capturedOutput.getOut().contains("received message is"));
		assertTrue(capturedOutput.getOut().contains("test account created"));
		Optional<Audit> order = auditRepository.findById(3L); // consume the message from the Postgres DB
		assertTrue(order.get().getId().equals(3L));
		assertTrue(order.get().getAuditEvent().equals("account created"));
		assertTrue(order.get().getAuditEventPayload().equals("test account created"));
	}

	@Test
	void shouldTestGetAllAuditMessages() throws MalformedURLException {
		URL baseUrl = new URL("http://localhost:" + port + "/audit");
		ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertTrue(response.getBody().contains("test customer1 created"));
		assertTrue(response.getBody().contains("test customer2 created"));
		assertTrue(response.getBody().contains("customer created"));
	}

	@Test
	void shouldTestGetAuditById() throws MalformedURLException{
		URL baseUrl = new URL("http://localhost:" + port + "/audit/1");
		ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertTrue(response.getBody().contains("customer created"));
		assertTrue(response.getBody().contains("test customer1 created"));
		assertTrue(response.getBody().contains("1"));
	}

	@Test
	void shoudTestCreatingAuditRecord() throws MalformedURLException{
		URL baseUrl = new URL("http://localhost:" + port + "/audit");
		Audit testAudit = auditRecordFactory(5L, "credit card created", "credit card for customer xxx created");
		ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(baseUrl.toString(), testAudit, String.class);
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertTrue(responseEntity.getBody().contains("5"));
		assertTrue(responseEntity.getBody().contains("credit card created"));
	}

	private Audit auditRecordFactory(Long id, String auditEvent, String auditEventPayload){
		return Audit
				.builder()
				.id(id)
				.auditEvent(auditEvent)
				.auditEventPayload(auditEventPayload)
				.auditEventDate(now.toString())
				.build();
	}
}

package com.cham.auditor;

import com.cham.auditor.domain.Audit;
import com.cham.auditor.repository.AuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class AuditorApplication implements CommandLineRunner {
	@Autowired
	private AuditRepository auditRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuditorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("loading some data to the Audit table");
		LocalDateTime now = LocalDateTime.now();
		Audit auditRecord1 = Audit
				.builder()
				.id(1L)
				.auditEvent("customer created")
				.auditEventPayload("test customer1 created")
				.auditEventDate(now.toString())
				.build();
		auditRepository.saveAll(Arrays.asList(auditRecord1));
	}

}

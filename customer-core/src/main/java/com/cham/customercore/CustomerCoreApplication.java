package com.cham.customercore;

import com.cham.customercore.domain.CustomerCore;
import com.cham.customercore.repository.CustomerCoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class CustomerCoreApplication implements CommandLineRunner {

	@Autowired
	private CustomerCoreRepository customerCoreRepository;

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

	public static void main(String[] args) {
		SpringApplication.run(CustomerCoreApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Inside the run method of the main class - CustomerCoreApplication");
		customerCoreRepository.saveAll(Arrays.asList(rob,tony));
	}
}

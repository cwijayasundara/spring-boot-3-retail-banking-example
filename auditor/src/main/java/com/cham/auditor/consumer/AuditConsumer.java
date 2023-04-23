package com.cham.auditor.consumer;

import com.cham.auditor.domain.Audit;
import com.cham.auditor.repository.AuditRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuditConsumer {
    private final Gson gson = new Gson();

    @Autowired
    private AuditRepository auditRepository;
    private static final String DATABASE_ACCESS_ERROR = "Error while saving order to the Postgres DB";

    @KafkaListener(topics = "${test.topic}")
    public Audit receive(String message) {
        Audit auditEvent = gson.fromJson(message, Audit.class);
        log.info("received message is " + auditEvent.toString());
        Audit savedAudit;
        try{
            savedAudit = auditRepository.save(auditEvent);
        } catch(DataAccessException dataAccessException){
            log.error(DATABASE_ACCESS_ERROR, dataAccessException.getStackTrace());
            throw new RuntimeException(DATABASE_ACCESS_ERROR, dataAccessException);
        }
        return savedAudit;
    }
}

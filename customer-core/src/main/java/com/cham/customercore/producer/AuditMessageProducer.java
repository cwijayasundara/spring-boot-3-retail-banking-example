package com.cham.customercore.producer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuditMessageProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String ERROR_WHILE_SENDING_ORDERS_TO_KAFKA = "Error while sending Orders to Kafka";
    public void send(String topic, String auditMessage) {
        log.info("sending payload for audit topic ='{}' to topic='{}'", auditMessage, topic);
        try {
            kafkaTemplate.send(topic, auditMessage);
            log.info("message sent to the audit topic");
        }catch(KafkaException kafkaException){
            log.error(ERROR_WHILE_SENDING_ORDERS_TO_KAFKA, kafkaException.getStackTrace());
            throw new RuntimeException(ERROR_WHILE_SENDING_ORDERS_TO_KAFKA, kafkaException);
        }
    }
}

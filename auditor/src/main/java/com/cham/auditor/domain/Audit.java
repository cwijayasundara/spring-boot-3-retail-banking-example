package com.cham.auditor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "audit")
public class Audit {
    @Id
    private Long id;

    @Column(name = "audit_event", nullable = false)
    private String auditEvent;

    @Column(name = "audit_event_payload", nullable = false)
    private String auditEventPayload;

    @Column(name = "audit_event_date", nullable = false)
    private String auditEventDate;
}





package com.cham.customercore.domain;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Audit {
    private Long id;
    private String auditEvent;
    private String auditEventPayload;
    private String auditEventDate;
}





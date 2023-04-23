package com.cham.customerservice.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerCore {
    private String id;
    private String ssn;
    private String firstName;
    private String lastName;
    private String accountNumber;
}

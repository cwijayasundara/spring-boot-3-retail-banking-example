package com.cham.customercore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
@Data
@RedisHash("customer-core")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerCore {
    private @Id String id;
    private @Indexed String ssn;
    private @Indexed String firstName;
    private @Indexed String lastName;
    private @Indexed String accountNumber;
}

package com.cham.customerservice.controller;

import com.cham.customerservice.client.CustomerCoreClient;
import com.cham.customerservice.domain.CustomerCore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerCoreClient customerCoreClient;

    private List<CustomerCore> customerList;

    @BeforeEach
    public void setUp() {
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
        customerList = Arrays.asList(rob, tony);
    }

    @Test
    public void findAllCustomersFromCoreAPI_success() {
        // Arrange
        when(customerCoreClient.findAllCustomers()).thenReturn(customerList);

        // Act
        List<CustomerCore> result = customerController.findAllCustomersFromCoreAPI();

        // Assert
        verify(customerCoreClient, times(1)).findAllCustomers();
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(customerList.get(0));
        assertThat(result.get(1)).isEqualToComparingFieldByField(customerList.get(1));
    }
}

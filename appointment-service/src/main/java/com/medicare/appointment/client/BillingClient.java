package com.medicare.appointment.client;

import com.medicare.appointment.dto.BillingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// "BILLING-SERVICE" resolved via Eureka service discovery + client-side load balancing
@FeignClient(name = "BILLING-SERVICE")
public interface BillingClient {

    @PostMapping("/billing/generate")
    BillingDto generateBill(@RequestParam("appointmentId") Long appointmentId,
                             @RequestParam("specialization") String specialization);
}

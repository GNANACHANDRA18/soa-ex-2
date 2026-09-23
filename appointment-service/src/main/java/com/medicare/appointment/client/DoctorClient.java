package com.medicare.appointment.client;

import com.medicare.appointment.dto.DoctorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// "DOCTOR-SERVICE" resolved via Eureka service discovery + client-side load balancing
@FeignClient(name = "DOCTOR-SERVICE")
public interface DoctorClient {

    @GetMapping("/doctors/{id}")
    DoctorDto getDoctorById(@PathVariable("id") Long id);

    @GetMapping("/doctors/{id}/available")
    Boolean isAvailable(@PathVariable("id") Long id);
}

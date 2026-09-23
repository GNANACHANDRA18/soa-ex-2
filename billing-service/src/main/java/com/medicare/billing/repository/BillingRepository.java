package com.medicare.billing.repository;

import com.medicare.billing.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByAppointmentId(Long appointmentId);
}

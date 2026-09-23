package com.medicare.billing.service;

import com.medicare.billing.entity.Billing;
import com.medicare.billing.repository.BillingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    // Simple consultation fee table keyed by specialization; used when generating a bill
    private static final Map<String, Double> FEE_TABLE = Map.of(
            "CARDIOLOGY", 800.0,
            "ORTHOPEDICS", 700.0,
            "DERMATOLOGY", 500.0
    );
    private static final double DEFAULT_FEE = 500.0;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public Billing generateBill(Long appointmentId, String specialization) {
        double amount = FEE_TABLE.getOrDefault(
                specialization == null ? "" : specialization.toUpperCase(), DEFAULT_FEE);
        Billing billing = new Billing(appointmentId, amount, "PENDING");
        return billingRepository.save(billing);
    }

    public List<Billing> getAllBills() {
        return billingRepository.findAll();
    }

    public Optional<Billing> getBillById(Long id) {
        return billingRepository.findById(id);
    }

    public Optional<Billing> getBillByAppointmentId(Long appointmentId) {
        return billingRepository.findByAppointmentId(appointmentId);
    }

    public Billing updatePaymentStatus(Long id, String status) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));
        billing.setPaymentStatus(status.toUpperCase());
        return billingRepository.save(billing);
    }
}

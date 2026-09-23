package com.medicare.billing.controller;

import com.medicare.billing.entity.Billing;
import com.medicare.billing.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Billing> generateBill(@RequestParam Long appointmentId,
                                                 @RequestParam(required = false) String specialization) {
        return new ResponseEntity<>(billingService.generateBill(appointmentId, specialization), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Billing> getAllBills() {
        return billingService.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillById(@PathVariable Long id) {
        return billingService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Billing> getByAppointment(@PathVariable Long appointmentId) {
        return billingService.getBillByAppointmentId(appointmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Billing> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.updatePaymentStatus(id, "PAID"));
    }
}

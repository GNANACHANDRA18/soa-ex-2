package com.medicare.billing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus; // PENDING, PAID

    public Billing() {}

    public Billing(Long appointmentId, Double amount, String paymentStatus) {
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}

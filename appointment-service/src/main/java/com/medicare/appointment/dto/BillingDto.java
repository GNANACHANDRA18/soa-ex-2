package com.medicare.appointment.dto;

public class BillingDto {
    private Long billId;
    private Long appointmentId;
    private Double amount;
    private String paymentStatus;

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}

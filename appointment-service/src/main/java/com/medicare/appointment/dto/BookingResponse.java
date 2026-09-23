package com.medicare.appointment.dto;

import com.medicare.appointment.entity.Appointment;

public class BookingResponse {
    private Appointment appointment;
    private BillingDto billing;

    public BookingResponse(Appointment appointment, BillingDto billing) {
        this.appointment = appointment;
        this.billing = billing;
    }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public BillingDto getBilling() { return billing; }
    public void setBilling(BillingDto billing) { this.billing = billing; }
}

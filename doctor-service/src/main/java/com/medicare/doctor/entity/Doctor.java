package com.medicare.doctor.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    /** e.g. "AVAILABLE", "UNAVAILABLE", or a slot description like "Mon-Fri 9AM-1PM" */
    @Column(nullable = false)
    private String availability;

    public Doctor() {}

    public Doctor(String name, String specialization, String availability) {
        this.name = name;
        this.specialization = specialization;
        this.availability = availability;
    }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}

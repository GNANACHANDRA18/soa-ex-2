package com.medicare.appointment.dto;

public class DoctorDto {
    private Long doctorId;
    private String name;
    private String specialization;
    private String availability;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}

package com.medicare.appointment.service;

import com.medicare.appointment.client.BillingClient;
import com.medicare.appointment.client.DoctorClient;
import com.medicare.appointment.dto.BillingDto;
import com.medicare.appointment.dto.BookingResponse;
import com.medicare.appointment.dto.DoctorDto;
import com.medicare.appointment.entity.Appointment;
import com.medicare.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Orchestrates the appointment booking lifecycle:
 * Appointment Service -> Doctor Service (validate doctor + availability + prevent conflicts)
 *                      -> Billing Service (generate consultation invoice)
 */
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorClient doctorClient;
    private final BillingClient billingClient;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               DoctorClient doctorClient,
                               BillingClient billingClient) {
        this.appointmentRepository = appointmentRepository;
        this.doctorClient = doctorClient;
        this.billingClient = billingClient;
    }

    public BookingResponse bookAppointment(Long patientId, Long doctorId, LocalDate date) {
        // 1. Inter-service call: validate doctor exists and is available
        DoctorDto doctor = doctorClient.getDoctorById(doctorId);
        if (doctor == null) {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }
        if (!"AVAILABLE".equalsIgnoreCase(doctor.getAvailability())) {
            throw new RuntimeException("Doctor " + doctor.getName() + " is not available for booking");
        }

        // 2. Eliminate scheduling conflicts: doctor cannot have two bookings same day
        List<Appointment> existing = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        boolean conflict = existing.stream().anyMatch(a -> !"CANCELLED".equalsIgnoreCase(a.getStatus()));
        if (conflict) {
            throw new RuntimeException("Scheduling conflict: Dr. " + doctor.getName()
                    + " already has an appointment on " + date);
        }

        // 3. Persist the appointment
        Appointment appointment = new Appointment(patientId, doctorId, date, "BOOKED");
        appointment = appointmentRepository.save(appointment);

        // 4. Inter-service call: generate the outpatient consultation bill
        BillingDto billing = billingClient.generateBill(appointment.getAppointmentId(), doctor.getSpecialization());

        return new BookingResponse(appointment, billing);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public Appointment cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus("COMPLETED");
        return appointmentRepository.save(appointment);
    }
}

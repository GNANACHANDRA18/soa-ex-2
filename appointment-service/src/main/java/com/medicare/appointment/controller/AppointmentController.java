package com.medicare.appointment.controller;

import com.medicare.appointment.dto.BookingResponse;
import com.medicare.appointment.entity.Appointment;
import com.medicare.appointment.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestParam Long patientId,
                                              @RequestParam Long doctorId,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            BookingResponse response = appointmentService.bookAppointment(patientId, doctorId, date);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatient(@PathVariable Long patientId) {
        return appointmentService.getByPatient(patientId);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Appointment> complete(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }
}

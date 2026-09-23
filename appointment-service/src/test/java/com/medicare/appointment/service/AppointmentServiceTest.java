package com.medicare.appointment.service;

import com.medicare.appointment.client.BillingClient;
import com.medicare.appointment.client.DoctorClient;
import com.medicare.appointment.dto.BillingDto;
import com.medicare.appointment.dto.BookingResponse;
import com.medicare.appointment.dto.DoctorDto;
import com.medicare.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Appointment -> Doctor -> Billing orchestration flow.
 * DoctorClient and BillingClient (Feign clients) are mocked to isolate
 * AppointmentService business logic from network/service discovery.
 */
class AppointmentServiceTest {

    private AppointmentRepository appointmentRepository;
    private DoctorClient doctorClient;
    private BillingClient billingClient;
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentRepository = Mockito.mock(AppointmentRepository.class);
        doctorClient = Mockito.mock(DoctorClient.class);
        billingClient = Mockito.mock(BillingClient.class);
        appointmentService = new AppointmentService(appointmentRepository, doctorClient, billingClient);
    }

    private DoctorDto availableDoctor() {
        DoctorDto dto = new DoctorDto();
        dto.setDoctorId(1L);
        dto.setName("Dr. Ananya Rao");
        dto.setSpecialization("Cardiology");
        dto.setAvailability("AVAILABLE");
        return dto;
    }

    @Test
    void testBookAppointment_success() {
        DoctorDto doctor = availableDoctor();
        Mockito.when(doctorClient.getDoctorById(1L)).thenReturn(doctor);
        Mockito.when(appointmentRepository.findByDoctorIdAndDate(1L, LocalDate.of(2026, 10, 1)))
                .thenReturn(Collections.emptyList());
        Mockito.when(appointmentRepository.save(Mockito.any()))
                .thenAnswer(inv -> {
                    var appt = inv.getArgument(0, com.medicare.appointment.entity.Appointment.class);
                    appt.setAppointmentId(100L);
                    return appt;
                });
        BillingDto billingDto = new BillingDto();
        billingDto.setBillId(500L);
        billingDto.setAmount(800.0);
        billingDto.setPaymentStatus("PENDING");
        Mockito.when(billingClient.generateBill(100L, "Cardiology")).thenReturn(billingDto);

        BookingResponse response = appointmentService.bookAppointment(10L, 1L, LocalDate.of(2026, 10, 1));

        assertEquals("BOOKED", response.getAppointment().getStatus());
        assertEquals(800.0, response.getBilling().getAmount());
    }

    @Test
    void testBookAppointment_doctorUnavailableThrows() {
        DoctorDto doctor = availableDoctor();
        doctor.setAvailability("UNAVAILABLE");
        Mockito.when(doctorClient.getDoctorById(1L)).thenReturn(doctor);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.bookAppointment(10L, 1L, LocalDate.of(2026, 10, 1)));
        assertTrue(ex.getMessage().contains("not available"));
    }

    @Test
    void testBookAppointment_conflictThrows() {
        DoctorDto doctor = availableDoctor();
        Mockito.when(doctorClient.getDoctorById(1L)).thenReturn(doctor);
        var existing = new com.medicare.appointment.entity.Appointment(20L, 1L, LocalDate.of(2026, 10, 1), "BOOKED");
        Mockito.when(appointmentRepository.findByDoctorIdAndDate(1L, LocalDate.of(2026, 10, 1)))
                .thenReturn(Collections.singletonList(existing));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.bookAppointment(10L, 1L, LocalDate.of(2026, 10, 1)));
        assertTrue(ex.getMessage().contains("conflict"));
    }
}

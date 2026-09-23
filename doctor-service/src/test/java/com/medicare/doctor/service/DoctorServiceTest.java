package com.medicare.doctor.service;

import com.medicare.doctor.entity.Doctor;
import com.medicare.doctor.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/** Unit tests for DoctorService using Mockito (no Spring context required). */
class DoctorServiceTest {

    private DoctorRepository doctorRepository;
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        doctorService = new DoctorService(doctorRepository);
    }

    @Test
    void testGetDoctorById_found() {
        Doctor doctor = new Doctor("Dr. Ananya Rao", "Cardiology", "AVAILABLE");
        doctor.setDoctorId(1L);
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = doctorService.getDoctorById(1L);

        assertTrue(result.isPresent());
        assertEquals("Dr. Ananya Rao", result.get().getName());
    }

    @Test
    void testIsAvailable_true() {
        Doctor doctor = new Doctor("Dr. Karthik Menon", "Orthopedics", "AVAILABLE");
        doctor.setDoctorId(2L);
        Mockito.when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        assertTrue(doctorService.isAvailable(2L));
    }

    @Test
    void testIsAvailable_falseWhenUnavailable() {
        Doctor doctor = new Doctor("Dr. Priya Sharma", "Dermatology", "UNAVAILABLE");
        doctor.setDoctorId(3L);
        Mockito.when(doctorRepository.findById(3L)).thenReturn(Optional.of(doctor));

        assertFalse(doctorService.isAvailable(3L));
    }

    @Test
    void testIsAvailable_falseWhenNotFound() {
        Mockito.when(doctorRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(doctorService.isAvailable(99L));
    }

    @Test
    void testCreateDoctor() {
        Doctor doctor = new Doctor("Dr. New Doc", "Neurology", "AVAILABLE");
        Mockito.when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor saved = doctorService.createDoctor(doctor);

        assertEquals("Dr. New Doc", saved.getName());
        Mockito.verify(doctorRepository, Mockito.times(1)).save(doctor);
    }

    @Test
    void testGetAllDoctors() {
        Mockito.when(doctorRepository.findAll()).thenReturn(List.of(
                new Doctor("Dr. A", "Cardiology", "AVAILABLE"),
                new Doctor("Dr. B", "Orthopedics", "AVAILABLE")
        ));

        List<Doctor> doctors = doctorService.getAllDoctors();
        assertEquals(2, doctors.size());
    }
}

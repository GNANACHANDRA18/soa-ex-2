package com.medicare.doctor.service;

import com.medicare.doctor.entity.Doctor;
import com.medicare.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationIgnoreCase(specialization);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updated) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctor.setName(updated.getName());
        doctor.setSpecialization(updated.getSpecialization());
        doctor.setAvailability(updated.getAvailability());
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public boolean isAvailable(Long id) {
        return doctorRepository.findById(id)
                .map(d -> "AVAILABLE".equalsIgnoreCase(d.getAvailability()))
                .orElse(false);
    }
}

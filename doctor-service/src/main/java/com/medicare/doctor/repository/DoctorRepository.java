package com.medicare.doctor.repository;

import com.medicare.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecializationIgnoreCase(String specialization);
}

package com.medicare.doctor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicare.doctor.entity.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test that boots the full Spring context (with in-memory H2 DB)
 * and exercises the Doctor REST API end-to-end through MockMvc.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class DoctorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndFetchDoctor() throws Exception {
        Doctor doctor = new Doctor("Dr. Integration Test", "Pediatrics", "AVAILABLE");

        String response = mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dr. Integration Test"))
                .andReturn().getResponse().getContentAsString();

        Doctor created = objectMapper.readValue(response, Doctor.class);

        mockMvc.perform(get("/doctors/" + created.getDoctorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialization").value("Pediatrics"));
    }

    @Test
    void testGetAllDoctors_returnsSeedData() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk());
    }
}

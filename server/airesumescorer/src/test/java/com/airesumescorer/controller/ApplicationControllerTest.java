package com.airesumescorer.controller;

import com.airesumescorer.dto.ScoreRequestDTO;
import com.airesumescorer.model.Application;
import com.airesumescorer.model.Job;
import com.airesumescorer.repository.ApplicationRepository;
import com.airesumescorer.repository.JobRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class) // test only http response from app controller
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc; // fake http post

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApplicationRepository applicationRepository;

    @MockitoBean
    private JobRepository jobRepository;

    @Test
    void scoreApplication_returnsCreatedWithScore() throws Exception {
        // Arrange
        ScoreRequestDTO dto = new ScoreRequestDTO();
        dto.setJobDescription("Software Engineer");
        dto.setResumeText("My resume text");
        dto.setSessionToken("abc123");

        Job mockJob = new Job();
        mockJob.setId(1L);
        mockJob.setDescription("Software Engineer");

        Application mockApp = new Application();
        mockApp.setId(1L);
        mockApp.setResumeText("My resume text");
        mockApp.setSessionToken("abc123");
        mockApp.setJob(mockJob);
        mockApp.setAiScore(75);
        mockApp.setAiFeedback("Good resume, improve your skills section");

        when(jobRepository.save(any(Job.class))).thenReturn(mockJob);
        when(applicationRepository.save(any(Application.class))).thenReturn(mockApp);

        // Act & Assert
        mockMvc.perform(post("/api/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.aiScore").value(75))
                .andExpect(jsonPath("$.aiFeedback").value("Good resume, improve your skills section"))
                .andExpect(jsonPath("$.sessionToken").value("abc123"));
    }

    @Test
    void scoreApplication_savesJobDescription() throws Exception {
        ScoreRequestDTO dto = new ScoreRequestDTO();
        dto.setJobDescription("Product Manager");
        dto.setResumeText("My resume");
        dto.setSessionToken("token456");

        Job mockJob = new Job();
        mockJob.setId(2L);
        mockJob.setDescription("Product Manager");

        Application mockApp = new Application();
        mockApp.setId(2L);
        mockApp.setJob(mockJob);
        mockApp.setAiScore(75);

        when(jobRepository.save(any(Job.class))).thenReturn(mockJob);
        when(applicationRepository.save(any(Application.class))).thenReturn(mockApp);

        mockMvc.perform(post("/api/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.job.description").value("Product Manager"));
    }
}
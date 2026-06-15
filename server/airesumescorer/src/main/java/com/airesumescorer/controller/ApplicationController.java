package com.airesumescorer.controller;

import com.airesumescorer.model.Job;
import com.airesumescorer.model.Application;
import com.airesumescorer.dto.ScoreRequestDTO;
import com.airesumescorer.dto.ScoreResultDTO;
import com.airesumescorer.repository.ApplicationRepository;
import com.airesumescorer.repository.JobRepository;
import com.airesumescorer.service.OpenAIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/score")
    public ResponseEntity<Application> scoreApplication(@RequestBody ScoreRequestDTO dto) {
        Job job = new Job();
        job.setDescription(dto.getJobDescription());
        Job savedJob = jobRepository.save(job);

        Application app = new Application();
        app.setResumeText(dto.getResumeText());
        app.setSessionToken(dto.getSessionToken());
        app.setJob(savedJob);

        try {
            String aiRaw = openAIService.scoreResume(dto.getResumeText(), dto.getJobDescription());
            System.out.println("Raw Groq response: " + aiRaw); // add this
            ScoreResultDTO result = objectMapper.readValue(aiRaw, ScoreResultDTO.class);
            app.setAiScore(result.getScore());
            app.setAiFeedback(result.getFeedback());
        } catch (Exception e) {
            System.out.println("Parsing error: " + e.getMessage()); // add this
            app.setAiScore(0);
            app.setAiFeedback("Scoring failed: " + e.getMessage());
        }

        Application savedApp = applicationRepository.save(app);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApp);
    }
}
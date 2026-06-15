package com.airesumescorer.controller;

import com.airesumescorer.model.Job;
import com.airesumescorer.model.Application;
import com.airesumescorer.dto.ScoreRequestDTO;
import com.airesumescorer.repository.ApplicationRepository;
import com.airesumescorer.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping("/score")
    public ResponseEntity<Application> scoreApplication(@RequestBody ScoreRequestDTO dto) {
        Job job = new Job();
        job.setDescription(dto.getJobDescription());
        Job savedJob = jobRepository.save(job);

        Application app = new Application();
        app.setResumeText(dto.getResumeText());
        app.setSessionToken(dto.getSessionToken());
        app.setJob(savedJob);

        // TODO: replace with real AI call
        int aiScore = 75;
        String aiFeedback = "Good resume, improve your skills section";

        app.setAiScore(aiScore);
        app.setAiFeedback(aiFeedback);
        Application savedApp = applicationRepository.save(app);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedApp);
    }
}
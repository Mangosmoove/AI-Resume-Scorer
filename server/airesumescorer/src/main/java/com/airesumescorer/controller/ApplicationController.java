package com.airesumescorer.controller;

import com.airesumescorer.model.Job;
import com.airesumescorer.model.Application;
import com.airesumescorer.dto.CheckDTO;
import com.airesumescorer.dto.ScoreRequestDTO;
import com.airesumescorer.dto.ScoreResultDTO;
import com.airesumescorer.repository.ApplicationRepository;
import com.airesumescorer.repository.JobRepository;
import com.airesumescorer.service.OpenAIService;
import com.airesumescorer.service.TikaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

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
    private TikaService tikaService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/score", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Application> scoreApplication(
            @RequestPart("jobDescription") String jobDescription,
            @RequestPart("sessionToken") String sessionToken,
            @RequestPart("resume") MultipartFile resume) {

        String resumeText = tikaService.extractText(resume);

        Job job = new Job();
        job.setDescription(jobDescription);
        Job savedJob = jobRepository.save(job);

        Application app = new Application();
        app.setResumeText(resumeText);
        app.setSessionToken(sessionToken);
        app.setJob(savedJob);

        try {
            String aiRaw = openAIService.scoreResume(resumeText, jobDescription);
            ScoreResultDTO result = objectMapper.readValue(aiRaw, ScoreResultDTO.class);

            // recalculate parent passed based on children bc groq has issues w calculation
            result.getSections().forEach((categoryName, category) -> {
                boolean allPassed = category.getChecks().values()
                        .stream()
                        .allMatch(CheckDTO::isPassed);
                category.setPassed(allPassed);
            });

            app.setAiScore(result.getScore());
            app.setAiSections(objectMapper.writeValueAsString(result.getSections()));
        } catch (Exception e) {
            app.setAiScore(0);
            app.setAiSections("{\"error\": \"Scoring failed: " + e.getMessage() + "\"}");
        }

        Application savedApp = applicationRepository.save(app);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApp);
    }
}

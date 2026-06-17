package com.airesumescorer.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //automatically increments
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable=false, updatable = false)
    private String sessionToken;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resumeText;

    @Column(nullable = false)
    private int aiScore;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String aiSections;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() { submittedAt = LocalDateTime.now(); } // gets time automatically before row

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }

    public int getAiScore() { return aiScore; }
    public void setAiScore(int aiScore) { this.aiScore = aiScore; }

    public String getAiSections() { return aiSections; }
    public void setAiSections(String aiSections) { this.aiSections = aiSections; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
}

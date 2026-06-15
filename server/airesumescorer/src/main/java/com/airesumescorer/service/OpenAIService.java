package com.airesumescorer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    @Value("${groq.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String scoreResume(String resumeText, String jobDescription) {
        String prompt = """
    You are a friendly and encouraging resume coach.
    
    Review the resume against the job description and score it from 0-100.
    Address the candidate directly using "you" and "your". Be warm, constructive,
    and motivating — even if the score is low, focus on what they can do to improve.
    
    Cover:
    - Why you gave them the score you did
    - What specific skills or keywords from the job description are missing from their resume
    - Concrete steps they can take to reach an 80+ score
    - Any strengths they already have that are worth highlighting
    
    Respond ONLY with valid JSON in this exact format:
    {
      "score": <integer 0-100>,
      "feedback": "<warm, direct, actionable feedback addressing the candidate as you>"
    }
    
    Job Description:
    %s
    
    Resume:
    %s
    """.formatted(jobDescription, resumeText);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a resume scoring assistant. Always respond with valid JSON only."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.3
        );

        String response = restClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response", e);
        }
    }
}
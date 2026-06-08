package com.airesumescorer.repository;

import com.airesumescorer.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findTop2ByJobIdAndSessionTokenOrderBySubmittedAtDesc(Long jobId, String sessionToken);
}
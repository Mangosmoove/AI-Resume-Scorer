package com.airesumescorer.dto;

import java.util.Map;

public class CategoryDTO {
    private boolean passed;
    private Map<String, CheckDTO> checks;

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public Map<String, CheckDTO> getChecks() { return checks; }
    public void setChecks(Map<String, CheckDTO> checks) { this.checks = checks; }
}

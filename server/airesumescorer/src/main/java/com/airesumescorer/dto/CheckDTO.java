package com.airesumescorer.dto;

public class CheckDTO {
    private boolean passed;
    private String notes;

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

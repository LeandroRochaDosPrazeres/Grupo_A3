package model;

import java.time.OffsetDateTime;

public class LogEntry {

    private Long id;
    private Long userId;
    private String action;
    private String details;
    private OffsetDateTime createdAt;

    public LogEntry() {
    }

    public LogEntry(Long id, Long userId, String action, String details, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public LogEntry(Long userId, String action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return createdAt + " - " + userId + " - " + action;
    }
}
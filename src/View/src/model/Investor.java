package model;

import java.time.OffsetDateTime;

public class Investor {

    private Long id;
    private String name;
    private String documentId;
    private RiskProfile riskProfile;
    private Long responsibleManagerId;
    private OffsetDateTime createdAt;

    public Investor() {
    }

    public Investor(Long id, String name, String documentId, RiskProfile riskProfile,
                    Long responsibleManagerId, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
        this.riskProfile = riskProfile;
        this.responsibleManagerId = responsibleManagerId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    public Long getResponsibleManagerId() {
        return responsibleManagerId;
    }

    public void setResponsibleManagerId(Long responsibleManagerId) {
        this.responsibleManagerId = responsibleManagerId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name + " - " + riskProfile;
    }
}
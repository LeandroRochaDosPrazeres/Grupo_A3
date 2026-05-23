package model;

import java.time.OffsetDateTime;

public class User {

    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private UserRole role;
    private String managerCode;
    private boolean active;
    private OffsetDateTime createdAt;

    public User() {}

    public User(Long id, String name, String email, String passwordHash,
                UserRole role, String managerCode, boolean active, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.managerCode = managerCode;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getManagerCode() { return managerCode; }
    public void setManagerCode(String managerCode) { this.managerCode = managerCode; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isManager() {
        return role == UserRole.MANAGER;
    }

    public boolean isInvestor() {
        return role == UserRole.INVESTOR;
    }

    public boolean checkPassword(String plainPassword) {
        if (plainPassword == null || passwordHash == null) return false;
        return passwordHash.equals(plainPassword);
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}

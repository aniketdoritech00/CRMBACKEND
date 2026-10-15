package com.doritech.microservices.ProductService.Response;

public class CustomerProductStats {
    private Long totalAssignments;
    private Long activeAssignments;
    private Long trialAssignments;
    private Long inactiveAssignments;
    private Long expiredAssignments;
    
    // Constructors
    public CustomerProductStats() {}
    
    public CustomerProductStats(Long totalAssignments, Long activeAssignments, Long trialAssignments, 
                               Long inactiveAssignments, Long expiredAssignments) {
        this.totalAssignments = totalAssignments;
        this.activeAssignments = activeAssignments;
        this.trialAssignments = trialAssignments;
        this.inactiveAssignments = inactiveAssignments;
        this.expiredAssignments = expiredAssignments;
    }
    
    // Getters and Setters
    public Long getTotalAssignments() { return totalAssignments; }
    public void setTotalAssignments(Long totalAssignments) { this.totalAssignments = totalAssignments; }
    
    public Long getActiveAssignments() { return activeAssignments; }
    public void setActiveAssignments(Long activeAssignments) { this.activeAssignments = activeAssignments; }
    
    public Long getTrialAssignments() { return trialAssignments; }
    public void setTrialAssignments(Long trialAssignments) { this.trialAssignments = trialAssignments; }
    
    public Long getInactiveAssignments() { return inactiveAssignments; }
    public void setInactiveAssignments(Long inactiveAssignments) { this.inactiveAssignments = inactiveAssignments; }
    
    public Long getExpiredAssignments() { return expiredAssignments; }
    public void setExpiredAssignments(Long expiredAssignments) { this.expiredAssignments = expiredAssignments; }
    
    @Override
    public String toString() {
        return "CustomerProductStats{" +
                "totalAssignments=" + totalAssignments +
                ", activeAssignments=" + activeAssignments +
                ", trialAssignments=" + trialAssignments +
                ", inactiveAssignments=" + inactiveAssignments +
                ", expiredAssignments=" + expiredAssignments +
                '}';
    }
}
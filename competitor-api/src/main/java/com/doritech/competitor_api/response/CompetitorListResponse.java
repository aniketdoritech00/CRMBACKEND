package com.doritech.competitor_api.response;
import java.util.List;

public class CompetitorListResponse {
    
    private List<CompetitorResponse> competitors;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    
    public CompetitorListResponse() {
    }
    
    public CompetitorListResponse(List<CompetitorResponse> competitors, int totalElements, 
                               int totalPages, int currentPage, int size) {
        this.competitors = competitors;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.size = size;
    }
    
    // Getters and Setters
    public List<CompetitorResponse> getCompetitors() {
        return competitors;
    }
    
    public void setCompetitors(List<CompetitorResponse> competitors) {
        this.competitors = competitors;
    }
    
    public int getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
}
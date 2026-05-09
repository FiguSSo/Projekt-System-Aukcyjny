package com.psa.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionRequestDto {
    private String title;
    private String description;
    private String category;
    private BigDecimal startPrice;
    private LocalDateTime endDate;
    private Long ownerId;

    // Konstruktory
    public AuctionRequestDto() {}

    // Gettery i Settery
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getStartPrice() { return startPrice; }
    public void setStartPrice(BigDecimal startPrice) { this.startPrice = startPrice; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
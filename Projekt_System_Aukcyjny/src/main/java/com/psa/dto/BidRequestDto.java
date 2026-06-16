package com.psa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class BidRequestDto {

    @NotNull(message = "Auction ID is required")
    private Long auctionId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Bid amount is required")
    @Positive(message = "Bid amount must be greater than 0")
    private BigDecimal amount;

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
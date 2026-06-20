package com.psa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class BidRequestDto {

    @NotNull(message = "ID aukcji jest wymagane")
    private Long auctionId;

    @NotNull(message = "ID użytkownika jest wymagane")
    private Long userId;

    @NotNull(message = "Kwota oferty jest wymagana")
    @Positive(message = "Kwota oferty musi być większa od zera")
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
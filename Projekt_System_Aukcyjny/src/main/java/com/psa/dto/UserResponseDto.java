package com.psa.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionRequestDto {
    private String title;
    private String description;
    private String category;
    private BigDecimal startingPrice;
    private LocalDateTime endDate;
    private Long ownerId;
}
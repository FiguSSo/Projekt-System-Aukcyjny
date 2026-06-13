package com.psa.service;

import com.psa.model.Bid;

public interface BidService {
    Bid placeBid(Long auctionId, Long userId, Bid bid);
}
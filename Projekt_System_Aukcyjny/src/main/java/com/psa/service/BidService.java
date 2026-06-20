// src/main/java/com/psa/service/BidService.java
package com.psa.service;

import com.psa.model.Bid;

import java.util.List;

public interface BidService {

    Bid placeBid(Long auctionId, Long userId, Bid bid);

    Bid getBidById(Long id);

    List<Bid> getAllBids();

    List<Bid> getBidsByAuctionId(Long auctionId);

    List<Bid> getBidsByUserId(Long userId);
}
package com.psa.service;

import com.psa.dto.AuctionRequestDto;
import com.psa.model.Auction;

import java.util.List;

public interface AuctionService {

    Auction createAuction(AuctionRequestDto auctionRequestDto);

    Auction updateAuction(Long id, AuctionRequestDto auctionRequestDto);

    Auction getAuctionById(Long id);

    void deleteAuction(Long id);

    List<Auction> getAllAuctions(String category, String status);
}
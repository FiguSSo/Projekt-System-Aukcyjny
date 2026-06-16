package com.psa.controller;

import com.psa.dto.BidRequestDto;
import com.psa.model.Bid;
import com.psa.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<Bid> placeBid(@Valid @RequestBody BidRequestDto bidRequestDto) {
        
        Bid bid = new Bid();
        bid.setAmount(bidRequestDto.getAmount()); 

        
        Bid savedBid = bidService.placeBid(bidRequestDto.getAuctionId(), bidRequestDto.getUserId(), bid);

        return new ResponseEntity<>(savedBid, HttpStatus.CREATED);
    }
}
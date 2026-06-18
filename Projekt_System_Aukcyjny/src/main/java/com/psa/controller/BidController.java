// src/main/java/com/psa/controller/BidController.java
package com.psa.controller;

import com.psa.dto.BidRequestDto;
import com.psa.model.Bid;
import com.psa.service.BidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        Bid savedBid = bidService.placeBid(
                bidRequestDto.getAuctionId(),
                bidRequestDto.getUserId(),
                bid
        );

        return new ResponseEntity<>(savedBid, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bid> getBidById(@PathVariable Long id) {
        Bid bid = bidService.getBidById(id);
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Bid>> getAllBids(
            @RequestParam(required = false) Long auctionId,
            @RequestParam(required = false) Long userId
    ) {
        if (auctionId != null) {
            return new ResponseEntity<>(bidService.getBidsByAuctionId(auctionId), HttpStatus.OK);
        }

        if (userId != null) {
            return new ResponseEntity<>(bidService.getBidsByUserId(userId), HttpStatus.OK);
        }

        return new ResponseEntity<>(bidService.getAllBids(), HttpStatus.OK);
    }
}
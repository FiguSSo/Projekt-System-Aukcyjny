package com.psa.controller;

import com.psa.dto.AuctionRequestDto;
import com.psa.model.Auction;
import com.psa.service.AuctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody AuctionRequestDto auctionRequestDto) {
        Auction newAuction = auctionService.createAuction(auctionRequestDto);
        return new ResponseEntity<>(newAuction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable Long id, @RequestBody AuctionRequestDto auctionRequestDto) {
        Auction updatedAuction = auctionService.updateAuction(id, auctionRequestDto);
        return new ResponseEntity<>(updatedAuction, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        Auction auction = auctionService.getAuctionById(id);
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Auction>> getAllAuctions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status
    ) {
        List<Auction> auctions = auctionService.getAllAuctions(category, status);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }
}
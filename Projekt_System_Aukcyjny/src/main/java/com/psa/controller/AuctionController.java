package com.psa.controller;

import com.psa.dto.AuctionRequestDto;
import com.psa.model.Auction;
import com.psa.service.AuctionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Auction> createAuction(@Valid @RequestBody AuctionRequestDto auctionRequestDto) {
        Auction newAuction = auctionService.createAuction(auctionRequestDto);
        return new ResponseEntity<>(newAuction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auction> updateAuction(
            @PathVariable Long id,
            @Valid @RequestBody AuctionRequestDto auctionRequestDto
    ) {
        Auction updatedAuction = auctionService.updateAuction(id, auctionRequestDto);
        return ResponseEntity.ok(updatedAuction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        Auction auction = auctionService.getAuctionById(id);
        return ResponseEntity.ok(auction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Auction>> getAllAuctions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status
    ) {
        List<Auction> auctions = auctionService.getAllAuctions(category, status);
        return ResponseEntity.ok(auctions);
    }
}
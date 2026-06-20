package com.psa.controller;

import com.psa.dto.BidRequestDto;
import com.psa.model.Bid;
import com.psa.service.BidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping("/bids")
    public ResponseEntity<Bid> placeBid(@Valid @RequestBody BidRequestDto bidRequestDto) {
        return createBidResponse(
                bidRequestDto.getAuctionId(),
                bidRequestDto.getUserId(),
                bidRequestDto.getAmount()
        );
    }

    @PostMapping("/auctions/{auctionId}/bids")
    public ResponseEntity<Bid> placeBidForAuction(
            @PathVariable Long auctionId,
            @Valid @RequestBody BidRequestDto bidRequestDto
    ) {
        return createBidResponse(
                auctionId,
                bidRequestDto.getUserId(),
                bidRequestDto.getAmount()
        );
    }

    private ResponseEntity<Bid> createBidResponse(Long auctionId, Long userId, BigDecimal amount) {
        Bid bid = new Bid();
        bid.setAmount(amount);

        Bid savedBid = bidService.placeBid(auctionId, userId, bid);
        return new ResponseEntity<>(savedBid, HttpStatus.CREATED);
    }
}

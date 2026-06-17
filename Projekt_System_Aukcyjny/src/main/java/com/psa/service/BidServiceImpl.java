package com.psa.service;

import com.psa.exception.ResourceNotFoundException;
import com.psa.model.Auction;
import com.psa.model.AuctionStatus;
import com.psa.model.Bid;
import com.psa.model.User;
import com.psa.repository.AuctionRepository;
import com.psa.repository.BidRepository;
import com.psa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BidServiceImpl implements BidService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public BidServiceImpl(
            AuctionRepository auctionRepository,
            BidRepository bidRepository,
            UserRepository userRepository
    ) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Bid placeBid(Long auctionId, Long userId, Bid bid) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + auctionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new IllegalStateException("Auction is not active");
        }

        Bid currentHighestBid = auction.getCurrentHighestBid();
        BigDecimal currentPrice = currentHighestBid != null
                ? currentHighestBid.getAmount()
                : auction.getStartingPrice();

        if (bid.getAmount().compareTo(currentPrice) <= 0) {
            throw new IllegalArgumentException("Bid amount must be higher than the current highest bid or starting price");
        }

        bid.setAuction(auction);
        bid.setBidder(user);
        bid.setBidTime(LocalDateTime.now());

        Bid savedBid = bidRepository.save(bid);

        auction.setCurrentHighestBid(savedBid);
        auctionRepository.save(auction);

        return savedBid;
    }
}
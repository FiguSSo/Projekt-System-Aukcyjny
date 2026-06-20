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
import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException("Aukcja o id " + auctionId + " nie istnieje"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Uzytkownik o id " + userId + " nie istnieje"));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new IllegalStateException("Aukcja nie jest aktywna");
        }

        Bid currentHighestBid = auction.getCurrentHighestBid();
        BigDecimal currentPrice = currentHighestBid != null
                ? currentHighestBid.getAmount()
                : auction.getStartingPrice();

        if (bid.getAmount().compareTo(currentPrice) <= 0) {
            throw new IllegalArgumentException("Oferta musi byc wyzsza niz aktualnie najwyzsza oferta");
        }

        bid.setAuction(auction);
        bid.setBidder(user);
        bid.setBidTime(LocalDateTime.now());

        Bid savedBid = bidRepository.save(bid);
        auction.setCurrentHighestBid(savedBid);
        auctionRepository.save(auction);

        return savedBid;
    }

    @Override
    public Bid getBidById(Long id) {
        return null;
    }

    @Override
    public List<Bid> getAllBids() {
        return List.of();
    }

    @Override
    public List<Bid> getBidsByAuctionId(Long auctionId) {
        return List.of();
    }

    @Override
    public List<Bid> getBidsByUserId(Long userId) {
        return List.of();
    }
}
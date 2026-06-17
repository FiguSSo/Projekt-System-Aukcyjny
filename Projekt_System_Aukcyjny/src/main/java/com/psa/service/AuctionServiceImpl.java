package com.psa.service;

import com.psa.dto.AuctionRequestDto;
import com.psa.exception.ResourceNotFoundException;
import com.psa.model.Auction;
import com.psa.model.AuctionStatus;
import com.psa.model.Category;
import com.psa.repository.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    public AuctionServiceImpl(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Override
    public Auction createAuction(AuctionRequestDto auctionRequestDto) {
        Auction auction = new Auction();
        mapAuctionFields(auction, auctionRequestDto);
        auction.setStatus(AuctionStatus.ACTIVE);
        return auctionRepository.save(auction);
    }

    @Override
    public Auction updateAuction(Long id, AuctionRequestDto auctionRequestDto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + id));

        AuctionStatus currentStatus = auction.getStatus();
        mapAuctionFields(auction, auctionRequestDto);
        auction.setStatus(currentStatus);

        return auctionRepository.save(auction);
    }

    @Override
    public Auction getAuctionById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + id));
    }

    @Override
    public void deleteAuction(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + id));

        auction.setStatus(AuctionStatus.CLOSED);
        auctionRepository.save(auction);
    }

    @Override
    public List<Auction> getAllAuctions(String category, String status) {
        if (hasText(category) && hasText(status)) {
            return auctionRepository.findByCategoryAndStatus(
                    parseCategory(category),
                    parseStatus(status)
            );
        }

        if (hasText(category)) {
            return auctionRepository.findByCategory(parseCategory(category));
        }

        if (hasText(status)) {
            return auctionRepository.findByStatus(parseStatus(status));
        }

        return auctionRepository.findAll();
    }

    private void mapAuctionFields(Auction auction, AuctionRequestDto auctionRequestDto) {
        auction.setTitle(auctionRequestDto.getTitle().trim());
        auction.setDescription(auctionRequestDto.getDescription().trim());
        auction.setStartingPrice(auctionRequestDto.getStartingPrice());
        auction.setStartDate(auctionRequestDto.getStartDate());
        auction.setEndDate(auctionRequestDto.getEndDate());
        auction.setCategory(parseCategory(auctionRequestDto.getCategory()));
    }

    private Category parseCategory(String category) {
        try {
            return Category.valueOf(category.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }

    private AuctionStatus parseStatus(String status) {
        try {
            return AuctionStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
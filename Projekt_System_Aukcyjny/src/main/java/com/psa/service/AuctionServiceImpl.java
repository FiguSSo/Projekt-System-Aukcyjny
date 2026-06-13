package com.psa.service.impl;

import com.psa.dto.AuctionRequestDto;
import com.psa.exception.ResourceNotFoundException;
import com.psa.model.Auction;
import com.psa.model.AuctionStatus;
import com.psa.model.Category;
import com.psa.repository.AuctionRepository;
import com.psa.service.AuctionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    public AuctionServiceImpl(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Override
    public Auction createAuction(AuctionRequestDto auctionRequestDto) {
        Auction auction = new Auction();
        auction.setTitle(auctionRequestDto.getTitle());
        auction.setDescription(auctionRequestDto.getDescription());
        auction.setStartingPrice(auctionRequestDto.getStartingPrice());
        auction.setStartDate(auctionRequestDto.getStartDate());
        auction.setEndDate(auctionRequestDto.getEndDate());
        auction.setCategory(Category.valueOf(auctionRequestDto.getCategory()));
        auction.setStatus(AuctionStatus.ACTIVE);

        return auctionRepository.save(auction);
    }

    @Override
    public Auction updateAuction(Long id, AuctionRequestDto auctionRequestDto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + id));

        auction.setTitle(auctionRequestDto.getTitle());
        auction.setDescription(auctionRequestDto.getDescription());
        auction.setStartingPrice(auctionRequestDto.getStartingPrice());
        auction.setEndDate(auctionRequestDto.getEndDate());
        auction.setCategory(Category.valueOf(auctionRequestDto.getCategory()));

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
        if (category != null && status != null) {
            return auctionRepository.findByCategoryAndStatus(Category.valueOf(category), AuctionStatus.valueOf(status));
        } else if (category != null) {
            return auctionRepository.findByCategory(Category.valueOf(category));
        } else if (status != null) {
            return auctionRepository.findByStatus(AuctionStatus.valueOf(status));
        }
        return auctionRepository.findAll();
    }
}
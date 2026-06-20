package com.psa.repository;

import com.psa.model.Auction;
import com.psa.model.AuctionStatus;
import com.psa.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findByCategory(Category category);

    List<Auction> findByStatus(AuctionStatus status);

    List<Auction> findByCategoryAndStatus(Category category, AuctionStatus status);
}

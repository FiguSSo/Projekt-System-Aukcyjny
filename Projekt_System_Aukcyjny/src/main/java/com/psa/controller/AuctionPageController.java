package com.psa.controller;

import com.psa.dto.AuctionRequestDto;
import com.psa.model.Auction;
import com.psa.service.AuctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuctionPageController {

    private final AuctionService auctionService;

    public AuctionPageController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Auction> auctions = auctionService.getAllAuctions(null, null);

        model.addAttribute("auctions", auctions);
        model.addAttribute("auctionForm", new AuctionRequestDto());

        return "index";
    }

    @PostMapping("/auctions")
    public String createAuctionFromForm(@ModelAttribute AuctionRequestDto auctionForm) {
        auctionService.createAuction(auctionForm);

        return "redirect:/";
    }
}
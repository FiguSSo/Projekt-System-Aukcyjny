package com.psa.controller;

import com.psa.dto.AuctionRequestDto;
import com.psa.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuctionPageController {

    private final AuctionService auctionService;

    public AuctionPageController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("auctions", auctionService.getAllAuctions(null, null));
        if (!model.containsAttribute("auctionForm")) {
            model.addAttribute("auctionForm", new AuctionRequestDto());
        }
        return "index";
    }

    @PostMapping("/auctions")
    public String createAuctionFromForm(
            @Valid @ModelAttribute("auctionForm") AuctionRequestDto auctionForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (auctionForm.getStartDate() != null
                && auctionForm.getEndDate() != null
                && !auctionForm.getEndDate().isAfter(auctionForm.getStartDate())) {
            bindingResult.rejectValue(
                    "endDate",
                    "auction.endDate.invalid",
                    "Data zakończenia musi być późniejsza niż data rozpoczęcia"
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("auctions", auctionService.getAllAuctions(null, null));
            return "index";
        }

        auctionService.createAuction(auctionForm);
        return "redirect:/";
    }
}
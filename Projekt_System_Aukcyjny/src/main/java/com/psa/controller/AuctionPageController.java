package com.psa.controller;

import com.psa.dto.AuctionRequestDto;
import com.psa.dto.BidRequestDto;
import com.psa.dto.UserRequestDto;
import com.psa.model.Bid;
import com.psa.service.AuctionService;
import com.psa.service.BidService;
import com.psa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuctionPageController {

    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;

    public AuctionPageController(
            AuctionService auctionService,
            BidService bidService,
            UserService userService
    ) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        fillPageModel(model);

        if (!model.containsAttribute("auctionForm")) {
            model.addAttribute("auctionForm", new AuctionRequestDto());
        }

        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserRequestDto());
        }

        if (!model.containsAttribute("bidForm")) {
            model.addAttribute("bidForm", new BidRequestDto());
        }

        return "index";
    }

    @PostMapping("/users/register")
    public String registerUser(
            @Valid @ModelAttribute("userForm") UserRequestDto userForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            fillPageModel(model);
            model.addAttribute("auctionForm", new AuctionRequestDto());
            model.addAttribute("bidForm", new BidRequestDto());
            return "index";
        }

        try {
            userService.createUser(userForm);
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik został utworzony.");
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("user.create.error", ex.getMessage());
            fillPageModel(model);
            model.addAttribute("auctionForm", new AuctionRequestDto());
            model.addAttribute("bidForm", new BidRequestDto());
            return "index";
        }
    }

    @PostMapping("/auctions")
    public String createAuction(
            @Valid @ModelAttribute("auctionForm") AuctionRequestDto auctionForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
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
            fillPageModel(model);
            model.addAttribute("userForm", new UserRequestDto());
            model.addAttribute("bidForm", new BidRequestDto());
            return "index";
        }

        auctionService.createAuction(auctionForm);
        redirectAttributes.addFlashAttribute("successMessage", "Aukcja została utworzona.");
        return "redirect:/";
    }

    @PostMapping("/bids")
    public String placeBid(
            @Valid @ModelAttribute("bidForm") BidRequestDto bidForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            fillPageModel(model);
            model.addAttribute("auctionForm", new AuctionRequestDto());
            model.addAttribute("userForm", new UserRequestDto());
            return "index";
        }

        try {
            Bid bid = new Bid();
            bid.setAmount(bidForm.getAmount());

            bidService.placeBid(
                    bidForm.getAuctionId(),
                    bidForm.getUserId(),
                    bid
            );

            redirectAttributes.addFlashAttribute("successMessage", "Oferta została złożona.");
            return "redirect:/";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            fillPageModel(model);
            model.addAttribute("auctionForm", new AuctionRequestDto());
            model.addAttribute("userForm", new UserRequestDto());
            model.addAttribute("bidError", ex.getMessage());
            return "index";
        }
    }

    private void fillPageModel(Model model) {
        model.addAttribute("auctions", auctionService.getAllAuctions(null, null));
        model.addAttribute("users", userService.getAllUsers());
    }
}
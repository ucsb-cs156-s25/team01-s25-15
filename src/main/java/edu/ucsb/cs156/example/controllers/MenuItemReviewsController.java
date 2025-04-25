package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.MenuItemReview;
import edu.ucsb.cs156.example.entities.UCSBDate;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.MenuItemsReviewRepository;
import edu.ucsb.cs156.example.repositories.UCSBDateRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.eclipse.jetty.client.Request.CommitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * This is a REST controller for MenuItemReviews
 */

@Tag(name = "MenuItemReviews")
@RequestMapping("/api/MenuItemReviews")
@RestController
@Slf4j

public class MenuItemReviewsController extends ApiController {

    @Autowired
    MenuItemsReviewRepository menuItemsReviewRepository;

    /**
     * List all MenuItemReviews
     * 
     * @return an iterable of MenuItemReviews
     */
    @Operation(summary = "List all menuitemreviews")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<MenuItemReview> allMenuItemReviews() {
        Iterable<MenuItemReview> menuitemreviews = menuItemsReviewRepository.findAll();
        return menuitemreviews;
    }

    /**
     * Create a new date
     * 
     * @param itemId        id of item reviewed
     * @param reviewerEmail email of reviewer
     * @param stars         number of stars given for item
     * @param dateReviewed  date item was reviewed
     * @param comments      additional comments
     * @return the saved MenuItemReview
     */
    @Operation(summary = "Create a new MenuItemReview")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public MenuItemReview postMenuItemReview(
            @Parameter(name = "itemId") @RequestParam Long itemId,
            @Parameter(name = "reviewerEmail") @RequestParam String reviewerEmail,
            @Parameter(name = "stars") @RequestParam int stars,
            @Parameter(name = "dateReviewed") @RequestParam("dateReviewed") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateReviewed,
            @Parameter(name = "comments") @RequestParam String comments)
            throws JsonProcessingException {

        // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        // See: https://www.baeldung.com/spring-date-parameters

        log.info("dateReviewed={}", dateReviewed);

        MenuItemReview menuItemReview = new MenuItemReview();
        menuItemReview.setItemId(itemId);
        menuItemReview.setReviewerEmail(reviewerEmail);
        menuItemReview.setStars(stars);
        menuItemReview.setDateReviewed(dateReviewed);
        menuItemReview.setComments(comments);

        MenuItemReview savedMenuItemReview = menuItemsReviewRepository.save(menuItemReview);

        return savedMenuItemReview;
    }

    /**
     * Get a single menuitemreview by id
     * 
     * @param id the id of the menuitemreview
     * @return a MenuItemReview
     */
    @Operation(summary= "Get a single menuitemreview")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public MenuItemReview getById(
            @Parameter(name="id") @RequestParam Long id) {
        MenuItemReview menuItemReview = menuItemsReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MenuItemReview.class, id));

        return menuItemReview;
    }

}

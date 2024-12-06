package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.service.ReviewImageService;
import UOSense.UOSense_Backend.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰 관리")
@RestController
@RequestMapping("/api/v1/Review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
}

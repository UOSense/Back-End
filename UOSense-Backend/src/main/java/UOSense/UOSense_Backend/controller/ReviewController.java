package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.dto.ReviewList;
import UOSense.UOSense_Backend.service.ReviewImageService;
import UOSense.UOSense_Backend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "리뷰 관리")
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰를 성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "삭제할 리뷰를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<Void> delete(@PathVariable int reviewId) {
        try {
            reviewService.delete(reviewId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/list")
    public ResponseEntity<ReviewList> getListByRestaurantId(@RequestParam int restaurantId) {
        try {
            ReviewList reviewList = reviewService.findListByRestaurantId(restaurantId);
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/like")
    @Operation(summary = "리뷰 좋아요", description = "메뉴의 좋아요 수를 1 증가시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 추천 수가 1 증가하였습니다."),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<Void> addLike(@RequestParam int userId,
                                        @RequestParam int reviewId) {
        try {
            reviewService.addLike(userId, reviewId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

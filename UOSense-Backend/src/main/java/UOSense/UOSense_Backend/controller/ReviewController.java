package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.service.ReviewImageService;
import UOSense.UOSense_Backend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰 관리")
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "메뉴 삭제", description = "메뉴를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴를 성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "삭제할 메뉴를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "네트워크 연결에 문제가 생겼습니다.")
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
}

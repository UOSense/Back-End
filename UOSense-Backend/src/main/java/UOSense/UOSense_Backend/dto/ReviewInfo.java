package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class ReviewInfo {
    private int id;

    private User user;

    private String title;

    private String body;

    private double rating;

    private LocalDateTime dateTime;

    private boolean reviewEventCheck;

    private String tag;

    private int likeCount;

    public static ReviewInfo from(Review review) {
        return ReviewInfo.builder()
                .id(review.getId())
                .user(review.getUser())
                .body(review.getBody())
                .rating(review.getRating())
                .dateTime(review.getDateTime())
                .reviewEventCheck(review.isReviewEventChecked())
                .tag(review.getTag().getValue())
                .likeCount(review.getLikeCount())
                .build();
    }
}

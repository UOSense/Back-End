package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.common.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewRequest {
    private int restaurantId;

    private String body;

    private double rating;

    private LocalDateTime dateTime;

    private boolean isReviewEventCheck;

    private Tag tag;
}

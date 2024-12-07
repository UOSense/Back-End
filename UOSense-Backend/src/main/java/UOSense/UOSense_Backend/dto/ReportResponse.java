package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Report;
import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
public class ReportResponse {
    private int id;
    private Review review;
    private String title;
    private User user;
    private String detail;
    private LocalDateTime createdAt;

    public static ReportResponse from(Report report) {
        return builder()
                .id(report.getId())
                .review(report.getReview())
                .title(report.getTitle())
                .user(report.getUser())
                .detail(report.getDetail().getValue())
                .createdAt(report.getCreatedAt())
                .build();
    }
}

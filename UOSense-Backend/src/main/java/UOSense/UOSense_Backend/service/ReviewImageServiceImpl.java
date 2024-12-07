package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.ImageUtils;
import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.entity.ReviewImage;
import UOSense.UOSense_Backend.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImageServiceImpl implements ReviewImageService{
    private final ReviewImageRepository reviewImageRepository;
    private final ImageUtils imageUtils;
    final String S3_FOLDER_NAME = "review";

    @Override
    public void register(Review review, List<MultipartFile> images) {
        for (MultipartFile image : images) {
            String path = null;
            if (!image.isEmpty()) {
                /* 1. 이미지 스토리지에 저장 */
                path = imageUtils.uploadImageToS3(image, S3_FOLDER_NAME);
            }
            /* 2. 엔티티 준비 */
            ReviewImage savingImg = ReviewImage.builder().review(review).imageUrl(path).build();
            /* 3. DB에 저장 */
            reviewImageRepository.save(savingImg);
        }
    }

    @Override
    public List<String> find(int reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(reviewId);
        return reviewImages.stream()
                .map(ReviewImage::getImageUrl)
                .toList();
    }
}

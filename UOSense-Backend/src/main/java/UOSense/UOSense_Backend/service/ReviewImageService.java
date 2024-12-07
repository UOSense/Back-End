package UOSense.UOSense_Backend.service;


import UOSense.UOSense_Backend.entity.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewImageService {
    void register(Review review, List<MultipartFile> images);
    List<String> find(int reviewId);
}

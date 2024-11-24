package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.RestaurantImagesResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantImageService {
    /**
     * 이미지 원본 파일은 스토리지에 저장하고 DB에 저장된 URL목록을 반환합니다.
     * @throws IllegalArgumentException 저장할 엔티티가 빈 경우
     */
    public RestaurantImagesResponse save(Restaurant restaurant, MultipartFile[] images);
}

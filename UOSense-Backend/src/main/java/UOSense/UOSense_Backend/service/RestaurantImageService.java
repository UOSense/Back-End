package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.RestaurantImagesResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantImageService {
    public RestaurantImagesResponse save(Restaurant restaurant, MultipartFile[] images);
}

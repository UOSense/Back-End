package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.ImageUtils;
import UOSense.UOSense_Backend.dto.ImageInfo;
import UOSense.UOSense_Backend.dto.RestaurantImagesResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.entity.RestaurantImage;
import UOSense.UOSense_Backend.repository.RestaurantImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantImageServiceImpl implements RestaurantImageService {
    private final RestaurantImageRepository restaurantImageRepository;
    private final ImageUtils imageUtils;
    final String S3_FOLDER_NAME = "restaurant";
    
    @Override
    public RestaurantImagesResponse save(Restaurant restaurant, MultipartFile[] images) {
        List<ImageInfo> imageList = new ArrayList<>();

        for (MultipartFile image : images) {
            /* 1. 이미지 스토리지에 저장 */
            String path = imageUtils.uploadImageToS3(image, S3_FOLDER_NAME);
            /* 2. 엔티티 준비 */
            RestaurantImage savingImg = RestaurantImage.builder().restaurant(restaurant).imageUrl(path).build();
            /* 3. DB에 저장 */
            RestaurantImage savedImg = restaurantImageRepository.save(savingImg);
            /* 4. 응답 데이터 준비 */
            imageList.add(getMetaData(savedImg));
        }
        return new RestaurantImagesResponse(restaurant.getId(), imageList);
    }
    public ImageInfo getMetaData(RestaurantImage image) {
        return new ImageInfo(image.getId(),image.getImageUrl());
    }
}

package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Utils.ImageUtils;
import UOSense.UOSense_Backend.repository.PurposeMenuRepository;
import UOSense.UOSense_Backend.repository.PurposeRestImgRepository;
import UOSense.UOSense_Backend.repository.PurposeRestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurposeServiceImpl implements PurposeService {
    private final PurposeRestRepository purposeRestRepository;
    private final PurposeRestImgRepository purposeRestImgRepository;
    private final PurposeMenuRepository purposeMenuRepository;
    private final ImageUtils imageUtils;

    @Override
    public void delete(int restaurantId) {
        if (!purposeRestRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("존재하지 않는 식당 정보입니다");
        }
        List<String> restaurantImages = purposeRestImgRepository.findImageUrls(restaurantId);
        List<String> menuImages = purposeMenuRepository.findImageUrls(restaurantId);

        restaurantImages.forEach(imageUtils::deleteImageInS3);
        menuImages.forEach(imageUtils::deleteImageInS3);

        purposeRestRepository.deleteById(restaurantId);
    }
}

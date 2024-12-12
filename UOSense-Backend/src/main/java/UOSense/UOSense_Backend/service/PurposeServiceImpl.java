package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Utils.ImageUtils;
import UOSense.UOSense_Backend.dto.PurposeRestListResponse;
import UOSense.UOSense_Backend.dto.PurposeRestResponse;
import UOSense.UOSense_Backend.entity.PurposeRest;
import UOSense.UOSense_Backend.entity.PurposeRestImg;
import UOSense.UOSense_Backend.repository.PurposeMenuRepository;
import UOSense.UOSense_Backend.repository.PurposeRestImgRepository;
import UOSense.UOSense_Backend.repository.PurposeRestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    @Override
    public List<PurposeRestListResponse> findAll() {
        List<PurposeRest> purposeRests = purposeRestRepository.findAll();
        if (purposeRests.isEmpty()) throw new NoSuchElementException("식당 리스트가 존재하지 않습니다.");
        return convertToListDTO(purposeRests);
    }

    @Override
    public PurposeRestResponse find(int restaurantId) {
        PurposeRest purposeRest = purposeRestRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당 제안이 존재하지 않습니다."));
        return PurposeRestResponse.from(purposeRest);
    }

    private List<PurposeRestListResponse> convertToListDTO(List<PurposeRest> purposeRests) {
        List<Integer> purposeRestIds = purposeRests.stream().map(PurposeRest::getId).toList();
        // restaurantId -> imageUrl 매핑 생성
        Map<Integer, String> imageList = purposeRestImgRepository.findAllFirstImageUrl(purposeRestIds)
                .stream().collect(Collectors.toMap(
                        image -> image.getPurposeRest().getId(), // Key: restaurantId
                        PurposeRestImg::getImageUrl            // Value: imageUrl
                ));
        // DTO 변환
        return purposeRests.stream()
                .map(purposeRest -> {
                    String imageUrl = imageList.getOrDefault(purposeRest.getId(), null);
                    return PurposeRestListResponse.from(purposeRest, imageUrl);
                })
                .toList();
    }
}

package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Utils.ImageUtils;
import UOSense.UOSense_Backend.dto.PurposeDayInfo;
import UOSense.UOSense_Backend.dto.PurposeDayList;
import UOSense.UOSense_Backend.dto.PurposeRestListResponse;
import UOSense.UOSense_Backend.dto.PurposeRestResponse;
import UOSense.UOSense_Backend.entity.PurposeDay;
import UOSense.UOSense_Backend.entity.PurposeRest;
import UOSense.UOSense_Backend.entity.PurposeRestImg;
import UOSense.UOSense_Backend.repository.PurposeDayRepository;
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
    private final PurposeDayRepository purposeDayRepository;

    @Override
    public void delete(int purposeRestId) {
        if (!purposeRestRepository.existsById(purposeRestId)) {
            throw new IllegalArgumentException("존재하지 않는 식당 정보입니다");
        }
        List<String> restaurantImages = purposeRestImgRepository.findImageUrls(purposeRestId);
        List<String> menuImages = purposeMenuRepository.findImageUrls(purposeRestId);

        restaurantImages.forEach(imageUtils::deleteImageInS3);
        menuImages.forEach(imageUtils::deleteImageInS3);

        purposeRestRepository.deleteById(purposeRestId);
    }

    @Override
    public List<PurposeRestListResponse> findAll() {
        List<PurposeRest> purposeRests = purposeRestRepository.findAll();
        if (purposeRests.isEmpty()) throw new NoSuchElementException("식당 리스트가 존재하지 않습니다.");
        return convertToListDTO(purposeRests);
    }

    @Override
    public PurposeRestResponse find(int purposeRestId) {
        PurposeRest purposeRest = purposeRestRepository.findById(purposeRestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당 제안이 존재하지 않습니다."));
        return PurposeRestResponse.from(purposeRest);
    }

    private List<PurposeRestListResponse> convertToListDTO(List<PurposeRest> purposeRests) {
        List<Integer> purposeRestIds = purposeRests.stream().map(PurposeRest::getId).toList();
        // purposeRestId -> imageUrl 매핑 생성
        Map<Integer, String> imageList = purposeRestImgRepository.findAllFirstImageUrl(purposeRestIds)
                .stream().collect(Collectors.toMap(
                        image -> image.getPurposeRest().getId(), // Key: purposeRestId
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

    @Override
    public PurposeDayList findPurposeDay(int purposeRestId) {
        List<PurposeDay> response = purposeDayRepository.findAllByRestaurantId(purposeRestId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException("식당 제안에 대한 영업 정보가 존재하지 않습니다.");
        }
        List<PurposeDayInfo> infoList = response.stream()
                .map(PurposeDayInfo::from)
                .toList();
        return new PurposeDayList(purposeRestId, infoList);
    }
}

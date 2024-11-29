package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.*;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.entity.RestaurantImage;
import UOSense.UOSense_Backend.repository.RestaurantImageRepository;
import UOSense.UOSense_Backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;
    @Override
    public List<RestaurantListResponse> search(String keyword, DoorType doorType) {
        List<Restaurant> result;
        // 1. 세부 분류 (e.g. 술집, 카페, 음식점) 내 검색
        if (Arrays.stream(SubDescription.values()).anyMatch(subDescription -> subDescription.getValue().equals(keyword))) {
            EnumBaseConverter<SubDescription> converter = new SubDescriptionConverter();
            SubDescription description = converter.convertToEntityAttribute(keyword);
            result = restaurantRepository.findByDoorTypeAndSubDescription(doorType,description);
        // 2. 음식 종류 (e.g. 한식, 중식) 내 검색
        } else if (Arrays.stream(Category.values()).anyMatch(category -> category.getValue().equals(keyword))) {
            EnumBaseConverter<Category> converter = new CategoryConverter();
            Category category = converter.convertToEntityAttribute(keyword);
            result = restaurantRepository.findByDoorTypeAndCategory(doorType,category);
        } else {    // 3. 메뉴명, 식당이름 (레벨슈타인 거리 알고리즘 이용)
            result = null;
        }

        if (result == null) {
            throw new NoSuchElementException("결과가 존재하지 않습니다.");
        }
        List<Restaurant> sortedList = sort(result, sortFilter.DEFAULT);
        return convertToListDTO(sortedList);
    }
}

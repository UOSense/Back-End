package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.*;

import UOSense.UOSense_Backend.dto.*;

import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;

import UOSense.UOSense_Backend.repository.MenuRepository;
import UOSense.UOSense_Backend.repository.RestaurantRepository;
import UOSense.UOSense_Backend.repository.RestaurantImageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService{
    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;
    private final MenuRepository menuRepository;

    @Override
    public List<RestaurantListResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (restaurants.isEmpty()) throw new NoSuchElementException("식당 리스트가 존재하지 않습니다.");
        List<RestaurantListResponse> responseList = restaurants.stream()
                .map(restaurant -> {
                    Optional<String> imageResponse = restaurantImageRepository.findFirstImageUrl(restaurant.getId());
                    String imageUrl = null;
                    if (imageResponse.isPresent()) imageUrl = imageResponse.get();
                    return RestaurantListResponse.from(restaurant, imageUrl);
                })
                .toList();
        return responseList;
    }

    @Override
    public List<RestaurantListResponse> getRestaurantsByFilter(DoorType doorType, Category category) {
        List<Restaurant> restaurants = restaurantRepository.findByDoorTypeAndCategory(doorType, category);
        if (restaurants.isEmpty()) throw new NoSuchElementException("해당 식당 리스트가 존재하지 않습니다.");
        List<RestaurantListResponse> responseList = restaurants.stream()
                .map(restaurant -> {
                    Optional<String> imageResponse = restaurantImageRepository.findFirstImageUrl(restaurant.getId());
                    String imageUrl = null;
                    if (imageResponse.isPresent()) imageUrl = imageResponse.get();
                    return RestaurantListResponse.from(restaurant, imageUrl);
                })
                .toList();
        return responseList;
    }

    @Override
    public List<RestaurantListResponse> getRestaurantsByCategory(Category category) {
        List<Restaurant> restaurants = restaurantRepository.findByCategory(category);
        if (restaurants.isEmpty()) throw new NoSuchElementException("해당 식당 리스트가 존재하지 않습니다.");
        List<RestaurantListResponse> responseList = restaurants.stream()
                .map(restaurant -> {
                    Optional<String> imageResponse = restaurantImageRepository.findFirstImageUrl(restaurant.getId());
                    String imageUrl = null;
                    if (imageResponse.isPresent()) imageUrl = imageResponse.get();
                    return RestaurantListResponse.from(restaurant, imageUrl);
                })
                .toList();
        return responseList;
    }

    @Override
    public List<RestaurantListResponse> getRestaurantsByDoorType(DoorType doorType) {
        List<Restaurant> restaurants = restaurantRepository.findByDoorType(doorType);
        if (restaurants.isEmpty()) throw new NoSuchElementException("해당 식당 리스트가 존재하지 않습니다.");
        List<RestaurantListResponse> responseList = restaurants.stream()
                .map(restaurant -> {
                    Optional<String> imageResponse = restaurantImageRepository.findFirstImageUrl(restaurant.getId());
                    String imageUrl = null;
                    if (imageResponse.isPresent()) imageUrl = imageResponse.get();
                    return RestaurantListResponse.from(restaurant, imageUrl);
                })
                .toList();
        return responseList;
    }

    @Override
    public RestaurantInfo getRestaurantInfoById(int restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        return RestaurantInfo.from(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(int restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));
    }

    @Override
    public List<MenuResponse> findMenuBy(int restaurantId) {
        List<Menu> menuBoard = menuRepository.findAllByRestaurantId((restaurantId));

        if (menuBoard.isEmpty())
            throw new IllegalArgumentException("해당 식당에 메뉴가 존재하지 않습니다.");

        return menuBoard.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }

    @Override
    @Transactional
    public void saveMenuWith(NewMenuRequest menu, String imageUrl) {
        Restaurant restaurant = restaurantRepository.findById(menu.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 식당입니다."));

        menuRepository.save(menu.toEntity(restaurant,imageUrl));
    }

    @Override
    @Transactional
    public void register(RestaurantRequest restaurantRequest) {
        double rating = 0.00;
        int reviewCount = 0;
        int bookmarkCount = 0;

        Restaurant restaurant = RestaurantRequest.toEntity(restaurantRequest, rating, reviewCount, bookmarkCount);

        restaurantRepository.save(restaurant);

    }

    @Override
    @Transactional
    public void edit(RestaurantRequest restaurantRequest) {
        double rating = 0.00;
        /**
         * rating 계산 code
         * review 기능 추가 시 추가
         */
        int reviewCount = 0;
        /**
         * reviewCount 계산 code
         * review 기능 추가 시 추가
         */
        int bookmarkCount = 0;
        /**
         * bookmarkCount 계산 code
         * bookmark 기능 추가 시 추가
         */

        if(!restaurantRepository.existsById(restaurantRequest.getId())) {
            throw new IllegalArgumentException("수정할 식당이 존재하지 않습니다.");
        }

        Restaurant restaurant = RestaurantRequest.toEntity(restaurantRequest, rating, reviewCount, bookmarkCount);

        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void delete(int restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("삭제할 식당이 존재하지 않습니다.");
        }
        restaurantRepository.deleteById(restaurantId);
    }
}

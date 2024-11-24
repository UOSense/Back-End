package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Category;
import UOSense.UOSense_Backend.common.DoorType;

import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.dto.RestaurantInfo;
import UOSense.UOSense_Backend.dto.MenuResponse;
import UOSense.UOSense_Backend.dto.NewMenuRequest;

import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;

import UOSense.UOSense_Backend.repository.MenuRepository;
import UOSense.UOSense_Backend.repository.RestaurantRepository;
import UOSense.UOSense_Backend.repository.RestaurantImageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
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
    public RestaurantInfo getRestaurantById(int RestaurantId) {
        Restaurant restaurant = restaurantRepository.findById(RestaurantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));

        return RestaurantInfo.from(restaurant);
    }

    @Override
    public List<MenuResponse> findMenuBy(int restaurantId) {
        List<Menu> menuBoard = menuRepository.findAllByRestaurantId((restaurantId));

        if (menuBoard.isEmpty())
            throw new IllegalArgumentException("해당 식당에 메뉴가 존재하지 않습니다.");

        return menuBoard.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void saveMenuWith(NewMenuRequest menu, String imageUrl) {
        Restaurant restaurant = restaurantRepository.findById(menu.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 식당입니다."));

        menuRepository.save(menu.toEntity(restaurant,imageUrl));
    }
}

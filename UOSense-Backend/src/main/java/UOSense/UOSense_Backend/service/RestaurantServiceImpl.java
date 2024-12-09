package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Utils.SearchUtils;
import UOSense.UOSense_Backend.common.enumClass.Category;
import UOSense.UOSense_Backend.common.enumClass.DoorType;
import UOSense.UOSense_Backend.dto.*;

import UOSense.UOSense_Backend.entity.BusinessDay;
import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;

import UOSense.UOSense_Backend.repository.BusinessDayRepository;
import UOSense.UOSense_Backend.repository.MenuRepository;
import UOSense.UOSense_Backend.repository.RestaurantRepository;

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
    private final MenuRepository menuRepository;
    private final BusinessDayRepository businessDayRepository;
    private final SearchService searchService;

    @Override
    public List<RestaurantListResponse> getAllRestaurants(SearchService.sortFilter filter) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (restaurants.isEmpty()) throw new NoSuchElementException("식당 리스트가 존재하지 않습니다.");
        return searchService.sort(restaurants, filter);
    }

    @Override
    public List<RestaurantListResponse> getRestaurantsByDoorType(DoorType doorType, SearchService.sortFilter filter) {
        List<Restaurant> restaurants = restaurantRepository.findByDoorType(doorType);
        if (restaurants.isEmpty()) throw new NoSuchElementException("식당 리스트가 존재하지 않습니다.");
        return searchService.sort(restaurants, filter);
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
    public void registerMenu(NewMenuRequest menu, String imageUrl) {
        Restaurant restaurant = restaurantRepository.findById(menu.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 식당입니다."));

        menuRepository.save(menu.toEntity(restaurant,imageUrl));
    }

    @Override
    @Transactional
    public void register(RestaurantRequest restaurantRequest) {

        Restaurant restaurant = RestaurantRequest.toEntity(restaurantRequest);

        restaurantRepository.save(restaurant);

    }

    @Override
    @Transactional
    public void edit(RestaurantRequest restaurantRequest) {

        if(!restaurantRepository.existsById(restaurantRequest.getId())) {
            throw new IllegalArgumentException("수정할 식당이 존재하지 않습니다.");
        }

        Restaurant restaurant = RestaurantRequest.toEntity(restaurantRequest);

        restaurantRepository.saveAndFlush(restaurant);
    }

    @Override
    @Transactional
    public void delete(int restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("삭제할 식당이 존재하지 않습니다.");
        }
        restaurantRepository.deleteById(restaurantId);
    }

    @Override
    public BusinessDayList findBusinessDay(int restaurantId) {
        List<BusinessDay> response = businessDayRepository.findAllByRestaurantId(restaurantId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException("식당에 대한 영업 정보가 존재하지 않습니다.");
        }
        List<BusinessDayInfo> infoList = response.stream()
                .map(BusinessDayInfo::from)
                .toList();
        return new BusinessDayList(restaurantId, infoList);
    }

    @Override
    @Transactional
    public void editBusinessDay(BusinessDayList businessDayList) {
        Restaurant restaurant = restaurantRepository.findById(businessDayList.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 식당입니다."));

        List<BusinessDayInfo> InfoList = businessDayList.getBusinessDayInfoList();
        for(BusinessDayInfo businessDayInfo : InfoList) {
            // breakTime이 없을 경우
            if (!businessDayInfo.isBreakTime()) {
                businessDayInfo.setBreakTime(null, null);
            }

            // id가 없을 경우
            if (!businessDayRepository.existsById(businessDayInfo.getId())) {
                throw new IllegalArgumentException("수정할 영업 정보를 찾을 수 없습니다.");
            }
            // id가 존재할 경우
            else {
                BusinessDay businessDay = BusinessDayInfo.toEntity(businessDayInfo, restaurant);
                businessDayRepository.saveAndFlush(businessDay);
            }
        }
    }

    @Override
    @Transactional
    public void saveBusinessDay(BusinessDayList businessDayList) {
        Restaurant restaurant = restaurantRepository.findById(businessDayList.getRestaurantId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 식당입니다."));

        List<BusinessDayInfo> InfoList = businessDayList.getBusinessDayInfoList();
        for(BusinessDayInfo businessDayInfo : InfoList) {
            // breakTime이 없을 경우
            if (!businessDayInfo.isBreakTime()) {
                businessDayInfo.setBreakTime(null, null);
            }

            // id가 존재할 경우
            if (businessDayRepository.existsById(businessDayInfo.getId())) {
                throw new IllegalArgumentException("영업 정보가 존재하는 Id입니다. 등록 대신 수정을 해주세요.");
            }
            // id가 없을 경우
            else {
                BusinessDay businessDay = BusinessDayInfo.toEntity(businessDayInfo, restaurant);
                businessDayRepository.save(businessDay);
            }
        }
    }

    @Override
    @Transactional
    public void deleteBusinessDay(int businessDayId) {
        if (!businessDayRepository.existsById(businessDayId)) {
            throw new IllegalArgumentException("삭제할 영업 정보가 존재하지 않습니다.");
        }
        businessDayRepository.deleteById(businessDayId);
    }
}

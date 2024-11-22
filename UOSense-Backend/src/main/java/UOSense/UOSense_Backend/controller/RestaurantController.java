package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.dto.RestaurantResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "식당 관리")
@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/show")
    @Operation(summary = "식당 정보 일괄 조회", description = "식당 리스트를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 리스트를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당 리스트를 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> getRestaurants(@RequestParam(required = false) DoorType doorType,
                                                                       @RequestParam(required = false) Restaurant.Category category) {
        List<RestaurantListResponse> restaurantList;
        boolean doorTypeFlag, categoryFlag;

        // doorType이 FRONT, SIDE, BACK일 경우, 해당 지역에 해당하는 식당 검색
        if (doorType == DoorType.FRONT || doorType == DoorType.SIDE || doorType == DoorType.BACK) {
            doorTypeFlag = true;
        }
        else if (doorType == null) {
            doorTypeFlag = false;
        }
        else { // enum에 없는 element
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }


        // category가 KOREAN, CHINESE, JAPANESE, WESTERN, OTHER 일 경우, 해당 category에 해당하는 식당 검색
        if (category == Restaurant.Category.Korean || category == Restaurant.Category.Chinese || category == Restaurant.Category.Japanese
            || category == Restaurant.Category.Western || category == Restaurant.Category.Other) {
            categoryFlag = true;
        }
        else if (category == null) {
            categoryFlag = false;
        }
        else { // enum에 없는 element
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }

        try {
            if (doorTypeFlag && categoryFlag) {
                restaurantList = restaurantService.getRestaurantsByFilter(doorType, category);
            }
            else if (doorTypeFlag) {
                restaurantList = restaurantService.getRestaurantsByDoorType(doorType);
            }
            else if (categoryFlag) {
                restaurantList = restaurantService.getRestaurantsByCategory(category);
            }
            else {
                restaurantList = restaurantService.getAllRestaurants();
            }

            return new ResponseEntity<>(restaurantList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{restaurantId}/show")
    @Operation(summary = "특정 식당 정보 조회", description = "식당 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 리스트를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.")
    })
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable int restaurantId) {
        try {
            RestaurantResponse restaurantResponse = restaurantService.getRestaurantById(restaurantId);

            return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

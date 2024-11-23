package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.dto.RestaurantInfo;
import UOSense.UOSense_Backend.dto.MenuResponse;
import UOSense.UOSense_Backend.dto.NewMenuRequest;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.service.ImageService;
import UOSense.UOSense_Backend.service.MenuService;
import UOSense.UOSense_Backend.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final MenuService menuService;
    private final ImageService imageService;

    @GetMapping("/show")
    @Operation(summary = "식당 정보 일괄 조회", description = "식당 리스트를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 리스트를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당 리스트를 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> getRestaurantList(@RequestParam(required = false) DoorType doorType,
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
            @ApiResponse(responseCode = "200", description = "식당 정보를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.")
    })
    public ResponseEntity<RestaurantInfo> getRestaurant(@PathVariable int restaurantId) {
        try {
            RestaurantInfo restaurantInfo = restaurantService.getRestaurantById(restaurantId);

            return new ResponseEntity<>(restaurantInfo, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{restaurantId}/menu")
    @Operation(summary = "특정 식당 메뉴 조회", description = "메뉴를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<List<MenuResponse>> showMenu(@PathVariable int restaurantId) {
        try {
            List<MenuResponse> result = restaurantService.findMenuBy(restaurantId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/uploadMenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "특정 식당 메뉴 등록", description = "메뉴를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "메뉴를 성공적으로 업로드하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<String> uploadMenu(@RequestPart List<NewMenuRequest> menus) {
        try {
            for ( NewMenuRequest menu : menus) {
                String imageUrl = imageService.saveNGetUrl(menu.getImage(), "menu");
                restaurantService.saveMenuWith(menu, imageUrl);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "메뉴 삭제", description = "메뉴를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴를 성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "404", description = "삭제할 메뉴를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<Void> deleteMenu(@PathVariable int id) {
        try {
            menuService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

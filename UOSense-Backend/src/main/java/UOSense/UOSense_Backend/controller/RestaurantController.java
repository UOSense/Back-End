package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.Category;
import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.*;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.service.MenuService;
import UOSense.UOSense_Backend.service.RestaurantImageService;
import UOSense.UOSense_Backend.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final RestaurantImageService restaurantImageService;

    @PostMapping("/restaurant/new")
    @Operation(summary = "신규 식당 등록", description = "새로운 식당을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "새로운 식당을 성공적으로 추가했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<Void> createRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        if (restaurantRequest.getId() != -1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            restaurantService.register(restaurantRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/restaurant/{restaurantId}/update")
    @Operation(summary = "기존 식당 정보 수정", description = "기존 식당 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기존 식당 정보를 성공적으로 수정했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당 정보를 찾을 수 없습니다.")
    })
    public ResponseEntity<Void> editRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        if (restaurantRequest.getId() == -1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            restaurantService.edit(restaurantRequest);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/show")
    @Operation(summary = "식당 정보 일괄 조회", description = "식당 리스트를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 리스트를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당 리스트를 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> getRestaurantList(@RequestParam(required = false) DoorType doorType,
                                                                          @RequestParam(required = false) Category category) {
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
        if (category == Category.KOREAN || category == Category.CHINESE || category == Category.JAPANESE
            || category == Category.WESTERN || category == Category.OTHER) {
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
            RestaurantInfo restaurantInfo = restaurantService.getRestaurantInfoById(restaurantId);

            return new ResponseEntity<>(restaurantInfo, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/restaurant/{restaurantId}/delete")
    @Operation(summary = "식당 삭제", description = "식당을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당을 성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "404", description = "삭제할 식당을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<Void> deleteRestaurant(@PathVariable int restaurantId) {
        try {
            restaurantService.delete(restaurantId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{restaurantId}/images")
    @Operation(summary = "특정 식당 사진 조회", description = "식당 사진을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 사진을 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "식당 사진을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<RestaurantImagesResponse> showImages(@PathVariable int restaurantId) {
        try {
            RestaurantImagesResponse restaurantImages = restaurantImageService.showImageList(restaurantId);
            return new ResponseEntity<>(restaurantImages, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{restaurantId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "특정 식당 사진 등록", description = "사진을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진을 성공적으로 저장했습니다."),
            @ApiResponse(responseCode = "410", description = "저장할 사진을 찾지 못해 실패했습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<RestaurantImagesResponse> createImages(
            @PathVariable int restaurantId,
            @RequestPart MultipartFile[] images) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
            RestaurantImagesResponse restaurantImages = restaurantImageService.save(restaurant, images);
            return new ResponseEntity<>(restaurantImages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.GONE);
        }
    }

    @GetMapping("/{restaurantId}/menu")
    @Operation(summary = "특정 식당 메뉴 조회", description = "메뉴를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<List<MenuResponse>> showMenuList(@PathVariable int restaurantId) {
        try {
            List<MenuResponse> result = restaurantService.findMenuBy(restaurantId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/menu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "특정 식당 메뉴 등록", description = "메뉴를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "메뉴를 성공적으로 업로드하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<String> uploadMenu(@RequestPart List<NewMenuRequest> menus) {
        try {
            for ( NewMenuRequest menu : menus) {
                String imageUrl = menuService.saveImage(menu.getImage());
                restaurantService.saveMenuWith(menu, imageUrl);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/menu")
    @Operation(summary = "특정 식당 메뉴 수정", description = "메뉴를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴를 성공적으로 수정하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "수정할 메뉴와 등록된 식당을 확인할 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<Void> updateMenu(@RequestBody MenuRequest menuRequest) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(menuRequest.getRestaurantId());
            menuService.edit(menuRequest, restaurant);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/menu/{id}")
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

package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "검색")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    @Operation(summary = "식당 검색", description = "식당을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> search(@RequestParam String keyword,
                                                               @RequestParam DoorType closestDoor) {
        try {
            List<Restaurant> restaurantList = searchService.findByKeyword(keyword); // restaurantList 캐시에 저장
            List<Restaurant> filteredResult = searchService.filterByDoorType(restaurantList, closestDoor);
            List<RestaurantListResponse> result = searchService.sort(filteredResult, SearchService.sortFilter.DEFAULT);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    /**
     * 호출 위치: 메인 지도 화면, 식당 목록 화면
     */
    @GetMapping("/filter")
    @Operation(summary = "검색결과 출입구문 필터링", description = "검색 결과를 출입구문 기준으로 필터링합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> filterByGate(@RequestParam String keyword,
                                                                     @RequestParam DoorType doorType) {
        try {
            List<Restaurant> cachedResult = searchService.checkRestaurantCache(keyword);
            List<Restaurant> filteredResult = searchService.filterByDoorType(cachedResult, doorType);
            List<RestaurantListResponse> result = searchService.sort(filteredResult, SearchService.sortFilter.DEFAULT);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    /**
     * 호출 위치: 식당 목록 화면
     */
    @GetMapping("/sort")
    @Operation(summary = "식당 목록 정렬", description = "검색 결과를 정렬합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과를 성공적으로 정렬하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> sortByFilter(@RequestParam String keyword,
                                                                     @RequestParam SearchService.sortFilter filter) {
        try {
            List<Restaurant> cachedResult = searchService.checkRestaurantCache(keyword);
            List<RestaurantListResponse> result = searchService.sort(cachedResult, filter);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

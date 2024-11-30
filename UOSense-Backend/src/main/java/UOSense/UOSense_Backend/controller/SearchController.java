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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "검색")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    @Operation(summary = "식당 검색", description = "입력어와 출입구문 필터를 바탕으로 식당을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과를 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.")
    })
    public ResponseEntity<List<RestaurantListResponse>> search(@RequestParam String keyword,
                                                                            @RequestParam DoorType closestDoor) {
        try {
            List<Restaurant> restaurantList = searchService.findByKeyword(keyword); // restaurantList 캐시에 저장
            List<Restaurant> filteredResult = searchService.filterByDoorType(keyword, closestDoor);
            List<RestaurantListResponse> result = searchService.sort(filteredResult, SearchService.sortFilter.DEFAULT);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<List<RestaurantListResponse>> filterByGate(@RequestParam String keyword,
                                                               @RequestParam DoorType doorType) {
        try {
            List<Restaurant> filteredResult = searchService.filterByDoorType(keyword, doorType);
            List<RestaurantListResponse> result = searchService.sort(filteredResult, SearchService.sortFilter.DEFAULT);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

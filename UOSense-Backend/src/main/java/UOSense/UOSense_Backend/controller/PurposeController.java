package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.dto.NewMenuRequest;
import UOSense.UOSense_Backend.dto.PurposeMenuRequest;
import UOSense.UOSense_Backend.dto.RestaurantImagesResponse;
import UOSense.UOSense_Backend.entity.PurposeMenu;
import UOSense.UOSense_Backend.entity.PurposeRestaurant;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.service.PurposeMenuService;
import UOSense.UOSense_Backend.service.PurposeRestImgServiceImpl;
import UOSense.UOSense_Backend.service.PurposeService;
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

import java.util.NoSuchElementException;

@Tag(name = "정보 수정 제안 관리")
@RestController
@RequestMapping("/api/v1/purpose")
@RequiredArgsConstructor
public class PurposeController {
    private final PurposeService purposeService;
    private final PurposeRestImgServiceImpl purposeRestImgService;
    private final PurposeMenuService purposeMenuService;

    @GetMapping("/get/images")
    @Operation(summary = "식당 정보 수정 제안 사진 조회", description = "사진을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진을 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "사진을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<RestaurantImagesResponse> getImages(@RequestParam int restaurantId) {
        try {
            RestaurantImagesResponse restaurantImages = purposeRestImgService.showImageList(restaurantId);
            return new ResponseEntity<>(restaurantImages, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/create/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "식당 정보 제안 사진 등록", description = "사진을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진을 성공적으로 저장했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "410", description = "저장할 사진을 찾지 못해 실패했습니다."),
            @ApiResponse(responseCode = "417", description = "저장할 사진을 찾지 못해 실패했습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<RestaurantImagesResponse> createImages(
            @RequestParam int restaurantId,
            @RequestPart MultipartFile[] images) {
        try {
            PurposeRestaurant purpose = purposeService.getPurposeRestById(restaurantId);
            RestaurantImagesResponse restaurantImages = purposeRestImgService.save(purpose,images);
            return new ResponseEntity<>(restaurantImages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.GONE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}

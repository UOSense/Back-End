package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.dto.PurposeRestListResponse;
import UOSense.UOSense_Backend.dto.PurposeRestResponse;
import UOSense.UOSense_Backend.service.PurposeService;
import com.amazonaws.SdkClientException;
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

@Tag(name = "정보 수정 제안 관리")
@RestController
@RequestMapping("/api/v1/purpose")
@RequiredArgsConstructor
public class PurposeController {
    private final PurposeService purposeService;

    @GetMapping("/get/restaurant/list")
    @Operation(summary = "정보 수정 제안 일괄 조회", description = "모든 정보 수정 제안을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 정보 수정 제안을 성공적으로 조회했습니다."),
            @ApiResponse(responseCode = "404", description = "정보 수정 제안이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<List<PurposeRestListResponse>> getPurposeRestaurantList() {
        try {
            List<PurposeRestListResponse> responses = purposeService.findAll();
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/restaurant")
    @Operation(summary = "정보 수정 제안 조회", description = "정보 수정 제안을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 제안을 성공적으로 조회했습니다."),
            @ApiResponse(responseCode = "404", description = "해당 정보 수정 제안이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<PurposeRestResponse> getPurposeRestaurant(@RequestParam int restaurantId) {
        try {
            PurposeRestResponse response = purposeService.find(restaurantId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "정보 수정 제안 삭제", description = "정보 수정 제안을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 제안을 성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "404", description = "삭제할 정보 수정 제안을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "417", description = "AWS S3에서 삭제에 실패했습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<Void> delete(@RequestParam int restaurantId){
        try {
            purposeService.delete(restaurantId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SdkClientException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}

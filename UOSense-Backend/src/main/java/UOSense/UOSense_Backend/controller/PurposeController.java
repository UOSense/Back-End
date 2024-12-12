package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.dto.*;
import UOSense.UOSense_Backend.entity.User;
import UOSense.UOSense_Backend.service.PurposeService;
import UOSense.UOSense_Backend.service.UserService;
import com.amazonaws.SdkClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "정보 수정 제안 관리")
@RestController
@RequestMapping("/api/v1/purpose")
@RequiredArgsConstructor
public class PurposeController {
    private final PurposeService purposeService;
    private final UserService userService;

    @PostMapping("/create/restaurant")
    @Operation(summary = "식당 제안 생성", description = "식당 제안을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 제안을 생성했습니다."),
            @ApiResponse(responseCode = "400", description = "제안 정보가 올바르지 않습니다."),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<Void> createRestaurant(@RequestBody PurposeRestRequest request, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        try {
            int userId = userService.findId(email);
            purposeService.register(request, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create/businessday")
    @Operation(summary = "영업 정보 제안 생성", description = "영업 정보 제안을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영업 정보 제안을 생성했습니다."),
            @ApiResponse(responseCode = "400", description = "제안 정보가 올바르지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<Void> createBusinessday(@RequestBody PurposeDayList purposeDayList) {
        try {
            purposeService.registerPurposeDay(purposeDayList);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
    public ResponseEntity<PurposeRestResponse> getPurposeRestaurant(@RequestParam int purposeRestId) {
        try {
            PurposeRestResponse response = purposeService.find(purposeRestId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/businessday")
    @Operation(summary = "영업 정보 제안 조회", description = "영업 정보 제안을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영업 정보 제안을 성공적으로 조회했습니다."),
            @ApiResponse(responseCode = "404", description = "영업 정보 제안이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<PurposeDayList> getPurposeDayList(@RequestParam int purposeRestId) {
        try {
            PurposeDayList purposeDayList = purposeService.findPurposeDay(purposeRestId);
            return new ResponseEntity<>(purposeDayList, HttpStatus.OK);
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
    public ResponseEntity<Void> delete(@RequestParam int purposeRestId){
        try {
            purposeService.delete(purposeRestId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SdkClientException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}

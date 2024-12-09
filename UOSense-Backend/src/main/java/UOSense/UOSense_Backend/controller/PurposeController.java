package UOSense.UOSense_Backend.controller;

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

@Tag(name = "정보 수정 제안 관리")
@RestController
@RequestMapping("/api/v1/purpose")
@RequiredArgsConstructor
public class PurposeController {
    private final PurposeService purposeService;

//    @GetMapping("/get/restaurant/list")
//    public ResponseEntity<List<>>
//
//    @GetMapping("/get/restaurant")

    @DeleteMapping("/delete")
    @Operation(summary = "정보 수정 제안 삭제", description = "정보 수정 제안을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 제안을 성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "400", description = "삭제할 정보 수정 제안을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "417", description = "AWS S3에서 삭제에 실패했습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    public ResponseEntity<Void> delete(@RequestParam int restaurantId){
        try {
            purposeService.delete(restaurantId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SdkClientException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}

package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.PurposeRestListResponse;
import UOSense.UOSense_Backend.dto.PurposeRestResponse;

import java.util.List;

public interface PurposeService {
    void delete(int restaurantId);
    List<PurposeRestListResponse> findAll();
    PurposeRestResponse find(int restaurantId);
}

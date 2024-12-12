package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.*;

import java.util.List;

public interface PurposeService {
    void delete(int restaurantId);
    List<PurposeRestListResponse> findAll();
    PurposeRestResponse find(int purposeRestId);
    PurposeDayList findPurposeDay(int purposeRestId);
    void register(PurposeRestRequest request, int userId);
    void registerPurposeDay(PurposeDayList purposeDayList);
}

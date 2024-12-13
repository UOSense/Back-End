package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.*;

import java.util.List;

public interface PurposeService {
    void register(PurposeRestRequest request, int userId);
    List<PurposeRestResponse> findList(int restaurantId);
    void delete(int purposeRestId);
}

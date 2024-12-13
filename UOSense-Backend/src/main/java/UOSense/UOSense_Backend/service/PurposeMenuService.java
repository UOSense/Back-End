package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.PurposeMenuRequest;
import UOSense.UOSense_Backend.dto.PurposeMenuResponse;
import UOSense.UOSense_Backend.entity.PurposeMenu;
import org.springframework.web.multipart.MultipartFile;

public interface PurposeMenuService {
    PurposeMenuResponse find(int purposeMenuId);
    public
    void register(PurposeMenuRequest request, MultipartFile menuImage);
    String saveImage(MultipartFile menuImage);
}

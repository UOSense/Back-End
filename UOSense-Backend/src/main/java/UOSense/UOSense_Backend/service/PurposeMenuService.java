package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.PurposeMenuRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PurposeMenuService {
    public void register(PurposeMenuRequest request, MultipartFile menuImage);
    public String saveImage(MultipartFile menuImage);
}

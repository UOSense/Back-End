package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.MenuRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MenuService {
    /** 스토리지에 이미지 저장 후 URL을 반환합니다. */
    String saveImage(MultipartFile file);
    void edit(MenuRequest menuRequest);
    void delete (int id);
}

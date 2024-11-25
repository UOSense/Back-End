package UOSense.UOSense_Backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface MenuService {
    String saveImage(MultipartFile file);
    void delete (int id);
}

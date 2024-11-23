package UOSense.UOSense_Backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    /**
     * 스토리지 내 주어진 이름의 폴더에 이미지를 저장합니다.
     */
    String saveNGetUrl (MultipartFile file, String folderName);
}

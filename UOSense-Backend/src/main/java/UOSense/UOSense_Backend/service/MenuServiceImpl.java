package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.ImageUtils;
import UOSense.UOSense_Backend.dto.MenuRequest;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
    private final MenuRepository menuRepository;
    private final ImageUtils imageUtils;
    final String S3_FOLDER_NAME = "menu";

    @Override
    public String saveImage(MultipartFile menuImage) {
        if (menuImage==null)
            return "";
        try {
            return imageUtils.uploadImageToS3(menuImage, S3_FOLDER_NAME);
        } catch ( RuntimeException e ) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void edit(MenuRequest menuRequest, Restaurant restaurant) {
        if (!menuRepository.existsById(menuRequest.getId())) {
            throw new IllegalArgumentException("수정할 메뉴가 존재하지 않습니다.");
        }
         menuRepository.save(menuRequest.toEntity(menuRequest,restaurant));
    }

    @Override
    public void delete(int menuId) {
        if (!menuRepository.existsById(menuId)) {
            // 댓글이 존재하지 않을 경우 예외를 던짐
            throw new IllegalArgumentException("삭제할 메뉴가 존재하지 않습니다.");
        }
        menuRepository.deleteById(menuId);
    }
}
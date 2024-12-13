package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Utils.ImageUtils;
import UOSense.UOSense_Backend.dto.PurposeMenuRequest;
import UOSense.UOSense_Backend.dto.PurposeMenuResponse;
import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.PurposeMenu;
import UOSense.UOSense_Backend.entity.User;
import UOSense.UOSense_Backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class PurposeMenuServiceImpl implements PurposeMenuService {
    private final PurposeMenuRepository purposeMenuRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    private final ImageUtils imageUtils;
    final String S3_FOLDER_NAME = "menu";

    @Override
    public PurposeMenuResponse find(int menuId) {
        PurposeMenu menu = purposeMenuRepository.findById((menuId))
                .orElseThrow(()-> new NoSuchElementException("해당 메뉴에 정보 수정 제안이 존재하지 않습니다."));

        return PurposeMenuResponse.from(menu);
    }

    @Override
    public void register(PurposeMenuRequest request, MultipartFile menuImage) {
        // purposeMenu가 null이면 신규 메뉴 등록 제안, 아니면 기존 메뉴 수정 제안이 되는 것.
        Menu purposeMenu = menuRepository.findById(request.getMenuId()).orElse(null);
        User proposer = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));
        String imageUrl = saveImage(menuImage);

        purposeMenuRepository.save(request.toEntity(purposeMenu, proposer, imageUrl));
    }

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
}

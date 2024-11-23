package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
    private final MenuRepository menuRepository;

    @Override
    public void delete(int menuId) {
        if (!menuRepository.existsById(menuId)) {
            // 댓글이 존재하지 않을 경우 예외를 던짐
            throw new IllegalArgumentException("삭제할 메뉴가 존재하지 않습니다.");
        }

        menuRepository.deleteById(menuId);
    }
}
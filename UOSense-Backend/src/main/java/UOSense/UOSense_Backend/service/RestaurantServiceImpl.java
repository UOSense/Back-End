package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.MenuResponse;
import UOSense.UOSense_Backend.dto.NewMenuRequest;
import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.repository.MenuRepository;
import UOSense.UOSense_Backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService{

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Override
    public List<MenuResponse> findMenuBy(int restaurantId) {
        List<Menu> menuBoard = menuRepository.findAllByRestaurantId((restaurantId));

        if (menuBoard.isEmpty())
            throw new IllegalArgumentException("해당 식당에 존재하지 않는 메뉴입니다.");

        return menuBoard.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}

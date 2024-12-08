package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuRequest {
    private int id;
    private int restaurantId;
    private String name;
    private int price;
    private String description;
    private MultipartFile image;

    public Menu toEntity(MenuRequest menuRequest, Restaurant restaurant) {
        return Menu.builder()
                .id(menuRequest.getId())
                .restaurant(restaurant)
                .name(this.name)
                .price(this.price)
                .description(this.getDescription())
                .build();
    }
}

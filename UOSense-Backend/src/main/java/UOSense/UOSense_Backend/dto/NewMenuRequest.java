package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
@Getter
public class NewMenuRequest {
    private int restaurantId;
    private String name;
    private int price;
    private String description;
    private MultipartFile image;

    public Menu toEntity(Restaurant restaurant, String url) {
        return Menu.builder()
                .restaurant(restaurant)
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .imageUrl(url)
                .build();
    }
}

package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class MenuResponse {
    private int menuId;
    private int restaurantId;
    private String name;
    private int price;
    private String description;
    private String imageUrl;
}

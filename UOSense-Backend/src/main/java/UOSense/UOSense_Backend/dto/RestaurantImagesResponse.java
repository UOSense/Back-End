package UOSense.UOSense_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantImagesResponse {
    private int restaurantId;
    private List<ImageInfo> ImageList;
}

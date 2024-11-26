package UOSense.UOSense_Backend.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
@Getter
public class MenuRequest {
    private int id;
    private int restaurantId;
    private String name;
    private int price;
    private String description;
    private MultipartFile image;
}

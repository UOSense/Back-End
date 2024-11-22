package UOSense.UOSense_Backend.dto;

import org.springframework.web.multipart.MultipartFile;

public class NewMenuRequest {
    private int restaurantId;
    private String name;
    private int price;
    private String description;
    private MultipartFile image;
}

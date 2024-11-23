package UOSense.UOSense_Backend.dto;

import org.springframework.web.multipart.MultipartFile;

public class MenuRequest {
    private int id;
    private int restaurantId;
    private String name;
    private int price;
    private String description;
    private MultipartFile image;
}

package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Restaurant;
import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class ReviewList {
    int restaurantId;
    List<ReviewInfo> reviewInfoList;
}

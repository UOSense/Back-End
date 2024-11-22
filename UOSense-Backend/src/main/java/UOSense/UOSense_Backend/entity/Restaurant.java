package UOSense.UOSense_Backend.entity;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.common.DoorTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Convert(converter = DoorTypeConverter.class)
    @Column(name = "door_type", nullable = false)
    private DoorType doorType;

    private double longitude;

    private double latitude;

    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;

    private double rating;

    @Enumerated(EnumType.STRING)
    private Category category;
    public enum Category { Korean, Chinese, Japanese, Western, Other}

    @Column(name = "sub_description")
    private String subDescription;

    private String description;

    @Column(name = "review_count", columnDefinition = "INT DEFAULT 0")
    private int reviewCount;

    @Column(name = "bookmark_count", columnDefinition = "INT DEFAULT 0")
    private int bookmarkCount;
}

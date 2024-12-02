package UOSense.UOSense_Backend.entity;

import UOSense.UOSense_Backend.common.*;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Convert(converter = CategoryConverter.class)
    public Category category;

    @Convert(converter = SubDescriptionConverter.class)
    @Column(name = "sub_description")
    private SubDescription subDescription;

    private String description;

    @Column(name = "review_count", columnDefinition = "INT DEFAULT 0")
    private int reviewCount;

    @Column(name = "bookmark_count", columnDefinition = "INT DEFAULT 0")
    private int bookmarkCount;

    public void setId(int id) {
        this.id = id;
    }
}

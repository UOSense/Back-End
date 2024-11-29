package UOSense.UOSense_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class BusinessDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)  // Restaurant와 다대일 관계
    @JoinColumn(name = "restaurant_id")  // 외래키 명시
    private Restaurant restaurant;

    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;
    public enum DayOfWeek {Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday}

    @Column(name = "have_break_time", nullable = false)
    private boolean haveBreakTime;

    @Column(name = "start_break_time")
    private LocalTime startBreakTime;

    @Column(name = "stop_break_time")
    private LocalTime stopBreakTime;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name ="closing_time")
    private LocalTime closingTime;

    @Column(name = "is_holiday", nullable = false)
    private boolean isHoliday;

    public void setId(int id) {
        this.id = id;
    }
}
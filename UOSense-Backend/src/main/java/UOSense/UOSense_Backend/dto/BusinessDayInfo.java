package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.BusinessDay;
import UOSense.UOSense_Backend.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BusinessDayInfo {
    private int id;

    private BusinessDay.DayOfWeek dayOfWeek;

    private boolean haveBreakTime;

    private LocalTime startBreakTime;

    private LocalTime stopBreakTime;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private boolean isHoliday;

    public static BusinessDayInfo from(BusinessDay businessDay) {
        return BusinessDayInfo.builder()
                .id(businessDay.getId())
                .dayOfWeek(businessDay.getDayOfWeek())
                .haveBreakTime(businessDay.isHaveBreakTime())
                .startBreakTime(businessDay.getStartBreakTime())
                .stopBreakTime(businessDay.getStopBreakTime())
                .openingTime(businessDay.getOpeningTime())
                .closingTime(businessDay.getClosingTime())
                .isHoliday(businessDay.isHoliday())
                .build();
    }

    public static BusinessDay toEntity(BusinessDayInfo businessDayInfo, Restaurant restaurant) {
        BusinessDay businessDay = BusinessDay.builder()
                .restaurant(restaurant)
                .dayOfWeek(businessDayInfo.getDayOfWeek())
                .haveBreakTime(businessDayInfo.isHaveBreakTime())
                .startBreakTime(businessDayInfo.getStartBreakTime())
                .stopBreakTime(businessDayInfo.getStopBreakTime())
                .openingTime(businessDayInfo.getOpeningTime())
                .closingTime(businessDayInfo.getClosingTime())
                .isHoliday(businessDayInfo.isHoliday())
                .build();

        if (businessDayInfo.getId() == -1) {
            // 신규 엔티티이므로 id 필드를 비워둠.
            return businessDay;
        }
        else {
            businessDay.setId(businessDayInfo.getId());
            return businessDay;
        }
    }
}

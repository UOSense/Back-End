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

    private String  startBreakTime;

    private String  stopBreakTime;

    private String openingTime;

    private String closingTime;

    private boolean isHoliday;

    public static BusinessDayInfo from(BusinessDay businessDay) {
        return BusinessDayInfo.builder()
                .id(businessDay.getId())
                .dayOfWeek(businessDay.getDayOfWeek())
                .haveBreakTime(businessDay.isHaveBreakTime())
                .startBreakTime(businessDay.getStartBreakTime().toString())
                .stopBreakTime(businessDay.getStopBreakTime().toString())
                .openingTime(businessDay.getOpeningTime().toString())
                .closingTime(businessDay.getClosingTime().toString())
                .isHoliday(businessDay.isHoliday())
                .build();
    }

    public static BusinessDay toEntity(BusinessDayInfo businessDayInfo, Restaurant restaurant) {
        BusinessDay businessDay = BusinessDay.builder()
                .restaurant(restaurant)
                .dayOfWeek(businessDayInfo.getDayOfWeek())
                .haveBreakTime(businessDayInfo.isHaveBreakTime())
                .startBreakTime(LocalTime.parse(businessDayInfo.getStartBreakTime()))
                .stopBreakTime(LocalTime.parse(businessDayInfo.getStopBreakTime()))
                .openingTime(LocalTime.parse(businessDayInfo.getOpeningTime()))
                .closingTime(LocalTime.parse(businessDayInfo.getClosingTime()))
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

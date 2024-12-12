package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.BusinessDay;
import UOSense.UOSense_Backend.entity.PurposeDay;
import UOSense.UOSense_Backend.entity.PurposeRest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurposeDayInfo {
    private int id;

    private BusinessDay.DayOfWeek dayOfWeek;

    private boolean breakTime;

    private String startBreakTime;

    private String stopBreakTime;

    private String openingTime;

    private String closingTime;

    private boolean holiday;

    public void setBreakTime() {
        this.startBreakTime = null;
        this.stopBreakTime = null;
    }

    public void setTime() {
        this.startBreakTime = null;
        this.stopBreakTime = null;
        this.openingTime = null;
        this.closingTime = null;
    }

    public static PurposeDayInfo from(PurposeDay purposeDay) {
        return PurposeDayInfo.builder()
                .id(purposeDay.getId())
                .dayOfWeek(purposeDay.getDayOfWeek())
                .breakTime(purposeDay.isBreakTime())
                .startBreakTime(String.valueOf(purposeDay.getStartBreakTime()))
                .stopBreakTime(String.valueOf(purposeDay.getStopBreakTime()))
                .openingTime(String.valueOf(purposeDay.getOpeningTime()))
                .closingTime(String.valueOf(purposeDay.getClosingTime()))
                .holiday(purposeDay.isHoliday())
                .build();
    }

    public static PurposeDay toEntity(PurposeDayInfo purposeDayInfo, PurposeRest purposeRest) {
        LocalTime startBreakTime = (purposeDayInfo.getStartBreakTime() != null) ? LocalTime.parse(purposeDayInfo.getStartBreakTime()) : null;
        LocalTime stopBreakTime = (purposeDayInfo.getStopBreakTime() != null) ? LocalTime.parse(purposeDayInfo.getStopBreakTime()) : null;
        LocalTime openingTime = (purposeDayInfo.getOpeningTime() != null) ? LocalTime.parse(purposeDayInfo.getOpeningTime()) : null;
        LocalTime closingTime = (purposeDayInfo.getClosingTime() != null) ? LocalTime.parse(purposeDayInfo.getClosingTime()) : null;
        return PurposeDay.builder()
                .purposeRest(purposeRest)
                .dayOfWeek(purposeDayInfo.getDayOfWeek())
                .breakTime(purposeDayInfo.isBreakTime())
                .startBreakTime(startBreakTime)
                .stopBreakTime(stopBreakTime)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .holiday(purposeDayInfo.isHoliday())
                .user(purposeRest.getUser())
                .build();
    }
}

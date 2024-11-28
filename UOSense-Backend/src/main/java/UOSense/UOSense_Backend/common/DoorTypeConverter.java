package UOSense.UOSense_Backend.common;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DoorTypeConverter extends EnumBaseConverter<DoorType> {
    public DoorTypeConverter() {
        super(DoorType.class);
    }
}

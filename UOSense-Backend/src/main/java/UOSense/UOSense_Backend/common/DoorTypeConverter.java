package UOSense.UOSense_Backend.common;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DoorTypeConverter extends EnumBaseConverter<DoorType> {
    protected DoorTypeConverter() {
        super(DoorType.class);
    }
}

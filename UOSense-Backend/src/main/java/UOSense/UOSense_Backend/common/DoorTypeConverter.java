package UOSense.UOSense_Backend.common;

import UOSense.UOSense_Backend.entity.Restaurant;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DoorTypeConverter implements AttributeConverter<DoorType, String> {
    @Override
    public String convertToDatabaseColumn(final DoorType doorType) {
        return doorType.getName();
    }

    @Override
    public DoorType convertToEntityAttribute(final String dbData) {
        return DoorType.fromValue(dbData);
    }
}

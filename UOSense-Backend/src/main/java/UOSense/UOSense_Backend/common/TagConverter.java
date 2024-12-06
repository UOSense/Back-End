package UOSense.UOSense_Backend.common;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TagConverter extends EnumBaseConverter<Tag> {
    public TagConverter() {
        super(Tag.class) ;
    }
}

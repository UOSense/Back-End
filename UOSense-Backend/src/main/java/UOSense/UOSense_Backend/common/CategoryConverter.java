package UOSense.UOSense_Backend.common;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryConverter extends EnumBaseConverter<Category>{
    protected CategoryConverter() {
        super(Category.class);
    }
}

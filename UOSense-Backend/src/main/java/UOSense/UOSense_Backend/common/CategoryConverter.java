package UOSense.UOSense_Backend.common;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryConverter extends EnumBaseConverter<Category>{
    public CategoryConverter() {
        super(Category.class);
    }
}

package pro.sky.star_bank.recommendation.model.enums;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumCompareTypeConverter implements Converter<String, EnumCompareType> {
    @Override
    public EnumCompareType convert(@NotNull String s) {
        return EnumCompareType.fromString(s);
    }
}

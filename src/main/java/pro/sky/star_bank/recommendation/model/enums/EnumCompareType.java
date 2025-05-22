package pro.sky.star_bank.recommendation.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
//@Getter
public enum EnumCompareType {
    GT(">"),
    LT("<"),
    EQ("="),
    GE(">="),
    LE("<=");

    private final String value;

    @JsonCreator
    public static EnumCompareType fromString(@NotEmpty String code) {
        return Arrays.stream(EnumCompareType.values())
                .filter(type -> type.value.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Нераспознанный код: " + code));
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
